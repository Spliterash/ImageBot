package ru.spliterash.imageBot.domain.def.executors;

import lombok.RequiredArgsConstructor;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import ru.spliterash.imageBot.domain.def.CaseExecutor;
import ru.spliterash.imageBot.domain.def.CaseIO;
import ru.spliterash.imageBot.domain.def.PipelineCase;
import ru.spliterash.imageBot.domain.def.annotation.NameUtils;
import ru.spliterash.imageBot.domain.def.cases.MultiDataCase;
import ru.spliterash.imageBot.domain.def.cases.SingleDataCase;
import ru.spliterash.imageBot.domain.def.params.CaseParams;
import ru.spliterash.imageBot.domain.entities.Data;
import ru.spliterash.imageBot.domain.exceptions.BotExceptionWrapper;
import ru.spliterash.imageBot.domain.exceptions.CaseValidateException;
import ru.spliterash.imageBot.domain.exceptions.ImageBotBaseException;
import ru.spliterash.imageBot.domain.validation.JavaXUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class DefaultCaseExecutor implements CaseExecutor {
    private final Validator validator;

    public DefaultCaseExecutor() {
        var factory = Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .buildValidatorFactory();
        validator = factory.getValidator();
        factory.close();
    }

    @Override
    public <C extends PipelineCase<P>, P extends CaseParams> CaseIO execute(C c, CaseIO io, P params) {
        try {
            return c.execute(io, validate(c, params));
        } catch (ImageBotBaseException ex) {
            throw ex;
        } catch (Exception e) {
            throw new BotExceptionWrapper(e);
        }
    }

    @Override
    public <C extends SingleDataCase<P, ID, OD>, P extends CaseParams, ID extends Data, OD extends Data> OD execute(C c, ID id, P params) {
        try {
            return c.process(id, validate(c, params));
        } catch (ImageBotBaseException ex) {
            throw ex;
        } catch (Exception e) {
            throw new BotExceptionWrapper(e);
        }
    }

    @Override
    public <C extends MultiDataCase<P, ID, OD>, P extends CaseParams, ID extends Data, OD extends Data> OD execute(C c, List<ID> id, P params) {
        return c.process(id, validate(c, params));
    }

    private <P extends CaseParams> P validate(PipelineCase<P> pipelineCase, P params) {
        Set<ConstraintViolation<P>> validate = validator.validate(params);

        // Всё чики пуки
        if (validate.isEmpty())
            return params;

        String caseName = NameUtils.name(pipelineCase.getClass());
        StringBuilder builder = new StringBuilder("Ошибка выполнения операции " + caseName + ". Список проблем:\n");

        for (ConstraintViolation<P> c : validate) {
            String fieldName = JavaXUtils.getFieldName(c);
            if (fieldName != null)
                builder.append(fieldName).append(": ");
            builder.append(c.getMessage()).append("\n");
        }

        throw new CaseValidateException(builder.toString(), new HashSet<>(validate));
    }
}
