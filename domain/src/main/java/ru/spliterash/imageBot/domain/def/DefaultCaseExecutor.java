package ru.spliterash.imageBot.domain.def;

import lombok.RequiredArgsConstructor;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import ru.spliterash.imageBot.domain.def.cases.SingleDataCase;
import ru.spliterash.imageBot.domain.def.params.CaseParams;
import ru.spliterash.imageBot.domain.entities.Data;

import javax.validation.Validation;
import javax.validation.Validator;

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
    public <C extends ICase<P>, P extends CaseParams> CaseIO execute(C c, CaseIO io, P params) {
        return c.execute(io, params);
    }

    @Override
    public <C extends SingleDataCase<P, ID, OD>, P extends CaseParams, ID extends Data, OD extends Data> OD execute(C c, ID id, P params) {
        return c.process(id, params);
    }
}
