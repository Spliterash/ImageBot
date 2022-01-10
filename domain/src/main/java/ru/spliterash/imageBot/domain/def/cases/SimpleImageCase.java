package ru.spliterash.imageBot.domain.def.cases;

import ru.spliterash.imageBot.domain.def.ImageCaseContext;
import ru.spliterash.imageBot.domain.def.ImagePipelineCase;
import ru.spliterash.imageBot.domain.def.params.CaseParams;
import ru.spliterash.imageBot.domain.entities.ImageData;

import java.util.ArrayList;
import java.util.List;

public abstract class SimpleImageCase<P extends CaseParams> implements ImagePipelineCase<P> {
    @Override
    public void execute(ImageCaseContext context, P params) throws Exception {
        List<ImageData> images = context.getImages();

        if (images.size() == 0)
            return;

        List<ImageData> result = new ArrayList<>(images.size());

        for (ImageData image : images) {
            ImageData imageData = process(image, params);

            result.add(imageData);
        }

        context.set(result);
    }

    public abstract ImageData process(ImageData input, P params) throws Exception;
}
