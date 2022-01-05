package ru.spliterash.imageBot.domain.def.cases.typed;

import ru.spliterash.imageBot.domain.def.cases.MultiDataCase;
import ru.spliterash.imageBot.domain.def.params.CaseParams;
import ru.spliterash.imageBot.domain.entities.ImageData;

public abstract class MultiImageCase<P extends CaseParams> extends MultiDataCase<P, ImageData, ImageData> {
}
