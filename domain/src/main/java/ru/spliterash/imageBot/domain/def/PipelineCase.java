package ru.spliterash.imageBot.domain.def;

import ru.spliterash.imageBot.domain.def.bean.Bean;
import ru.spliterash.imageBot.domain.def.params.CaseParams;

/**
 * Базовое обозначение кейса
 *
 * @param <P>
 */
public interface PipelineCase<P extends CaseParams> extends Bean {
    CaseIO execute(CaseIO io, P params) throws Exception;
}
