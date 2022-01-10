package ru.spliterash.imageBot.domain.def.executors;

import lombok.RequiredArgsConstructor;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import ru.spliterash.imageBot.domain.def.CaseExecutor;
import ru.spliterash.imageBot.domain.def.ImageCaseContext;
import ru.spliterash.imageBot.domain.def.ImagePipelineCase;
import ru.spliterash.imageBot.domain.def.annotation.NameUtils;
import ru.spliterash.imageBot.domain.def.cases.MultiImageCase;
import ru.spliterash.imageBot.domain.def.cases.SimpleImageCase;
import ru.spliterash.imageBot.domain.def.params.CaseParams;
import ru.spliterash.imageBot.domain.entities.ImageData;
import ru.spliterash.imageBot.domain.exceptions.BotExceptionWrapper;
import ru.spliterash.imageBot.domain.exceptions.CaseValidateException;
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


    private void validate(ImagePipelineCase<?> imagePipelineCase, CaseParams params) {
        Set<ConstraintViolation<CaseParams>> validate = validator.validate(params);

        // Всё чики пуки
        if (validate.isEmpty())
            return;

        String caseName = NameUtils.name(imagePipelineCase.getClass());
        StringBuilder builder = new StringBuilder("Ошибка выполнения операции " + caseName + ". Список проблем:\n");

        for (ConstraintViolation<?> c : validate) {
            String fieldName = JavaXUtils.getFieldName(c);
            if (fieldName != null)
                builder.append(fieldName).append(": ");
            builder.append(c.getMessage()).append("\n");
        }

        throw new CaseValidateException(builder.toString(), new HashSet<>(validate));
    }

    @Override
    public <C extends ImagePipelineCase<P>, P extends CaseParams> void execute(C c, ImageCaseContext context, P params) {
        validate(c, params);
        try {
            c.execute(context, params);
        } catch (Exception e) {
            throw new BotExceptionWrapper(e);
        }
    }

    @Override
    public <C extends SimpleImageCase<P>, P extends CaseParams> ImageData execute(C c, ImageData imageData, P params) {
        validate(c, params);
        try {
            return c.process(imageData, params);
        } catch (Exception e) {
            throw new BotExceptionWrapper(e);
        }
    }

    @Override
    public <C extends MultiImageCase<P>, P extends CaseParams> ImageData execute(C c, List<ImageData> images, P params) {
        validate(c, params);
        try {
            return c.process(images, params);
        } catch (Exception e) {
            throw new BotExceptionWrapper(e);
        }
    }
}
