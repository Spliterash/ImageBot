package ru.spliterash.imageBot.domain.def.cases;

import ru.spliterash.imageBot.domain.def.ImageCaseContext;
import ru.spliterash.imageBot.domain.def.ImagePipelineCase;
import ru.spliterash.imageBot.domain.def.params.CaseParams;
import ru.spliterash.imageBot.domain.entities.ImageData;

import java.util.Collections;
import java.util.List;

public abstract class MultiImageCase<P extends CaseParams> implements ImagePipelineCase<P> {
    @Override
    public final void execute(ImageCaseContext context, P params) throws Exception {
        if (context.getImages().size() == 0)
            return;

        ImageData result = process(context.getImages(), params);

        context.set(Collections.singletonList(result));
    }

    public abstract ImageData process(List<ImageData> data, P params) throws Exception;
}
