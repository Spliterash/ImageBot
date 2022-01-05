package ru.spliterash.imageBot.realization.image.imageIO.cases;

import lombok.RequiredArgsConstructor;
import ru.spliterash.imageBot.domain.cases.CoverImageUseCase;
import ru.spliterash.imageBot.domain.cases.GlueImagesCase;
import ru.spliterash.imageBot.domain.cases.ResizeCase;
import ru.spliterash.imageBot.domain.def.CaseIO;
import ru.spliterash.imageBot.domain.exceptions.ImageReadError;
import ru.spliterash.imageBot.realization.image.imageIO.in.ImageIOGlueImageCaseIn;

@RequiredArgsConstructor
public class ImageIoGlueImageCaseImpl implements GlueImagesCase {
    private final ResizeCase resizeCase;
    private final CoverImageUseCase coverImageUseCase;

    @Override
    public CaseIO execute(CaseIO io, GlueImagesInput params) throws ImageReadError {
        ImageIOGlueImageCaseIn in = new ImageIOGlueImageCaseIn(io, params, resizeCase, coverImageUseCase);

        return of(in.execute());
    }
}
