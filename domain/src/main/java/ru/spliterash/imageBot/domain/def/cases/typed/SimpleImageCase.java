package ru.spliterash.imageBot.domain.def.cases.typed;

import ru.spliterash.imageBot.domain.def.cases.SingleDataCase;
import ru.spliterash.imageBot.domain.def.params.CaseParams;
import ru.spliterash.imageBot.domain.entities.ImageData;

public abstract class SimpleImageCase<P extends CaseParams> extends SingleDataCase<P, ImageData, ImageData> {
}
