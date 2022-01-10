package ru.spliterash.imageBot.domain.def;

import ru.spliterash.imageBot.domain.def.bean.Bean;
import ru.spliterash.imageBot.domain.def.params.CaseParams;

/**
 * Базовое обозначение кейса
 *
 * @param <P>
 */
public interface ImagePipelineCase<P extends CaseParams> extends Bean {
    void execute(ImageCaseContext context, P params) throws Exception;

    default void validate(P params) {

    }
}
