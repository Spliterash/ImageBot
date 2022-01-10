package ru.spliterash.imageBot.realization.image.imageIO.cases.glue;

import lombok.RequiredArgsConstructor;
import ru.spliterash.imageBot.domain.cases.CoverImageUseCase;
import ru.spliterash.imageBot.domain.cases.ComicImageCase;
import ru.spliterash.imageBot.domain.cases.ProportionResizeCase;
import ru.spliterash.imageBot.domain.def.CaseExecutor;
import ru.spliterash.imageBot.domain.entities.ImageData;

import java.util.List;

@RequiredArgsConstructor
public class ComicImageCaseImpl extends ComicImageCase {
    private final CaseExecutor executor;
    private final ProportionResizeCase proportionResizeCase;
    private final CoverImageUseCase coverImageUseCase;


    @Override
    public ImageData process(List<ImageData> list, Params params) {
        ComicImageCaseIn in = new ComicImageCaseIn(list, params, executor, proportionResizeCase, coverImageUseCase);

        return in.execute();
    }
}
