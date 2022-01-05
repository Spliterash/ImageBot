package ru.spliterash.imageBot.realization.image.imageIO.cases;

import lombok.RequiredArgsConstructor;
import ru.spliterash.imageBot.domain.cases.CoverImageUseCase;
import ru.spliterash.imageBot.domain.cases.GlueImagesCase;
import ru.spliterash.imageBot.domain.cases.ResizeCase;
import ru.spliterash.imageBot.domain.def.CaseExecutor;
import ru.spliterash.imageBot.domain.entities.ImageData;
import ru.spliterash.imageBot.realization.image.imageIO.in.ImageIOGlueImageCaseIn;

import java.util.List;

@RequiredArgsConstructor
public class ImageIoGlueImageCaseImpl extends GlueImagesCase {
    private final CaseExecutor executor;
    private final ResizeCase resizeCase;
    private final CoverImageUseCase coverImageUseCase;


    @Override
    public ImageData process(List<ImageData> list, GlueImagesParams params) {
        ImageIOGlueImageCaseIn in = new ImageIOGlueImageCaseIn(list, params, executor, resizeCase, coverImageUseCase);

        return in.execute();
    }
}
