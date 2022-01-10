package ru.spliterash.imageBot.domain.def;

import ru.spliterash.imageBot.domain.def.cases.MultiImageCase;
import ru.spliterash.imageBot.domain.def.cases.SimpleImageCase;
import ru.spliterash.imageBot.domain.def.params.CaseParams;
import ru.spliterash.imageBot.domain.entities.ImageData;

import java.util.List;

public interface CaseExecutor {
    <C extends ImagePipelineCase<P>, P extends CaseParams> void execute(C c, ImageCaseContext context, P params);

    <C extends SimpleImageCase<P>, P extends CaseParams> ImageData execute(C c, ImageData imageData, P params);

    <C extends MultiImageCase<P>, P extends CaseParams> ImageData execute(C c, List<ImageData> images, P params);
}
