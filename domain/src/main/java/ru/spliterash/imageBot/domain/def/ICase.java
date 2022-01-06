package ru.spliterash.imageBot.domain.def;

import ru.spliterash.imageBot.domain.def.params.CaseParams;
import ru.spliterash.imageBot.domain.exceptions.ImageReadError;

/**
 * Базовое обозначение кейса
 *
 * @param <P>
 */
public interface ICase<P extends CaseParams> extends Bean {
    CaseIO execute(CaseIO io, P params) throws ImageReadError;
}
