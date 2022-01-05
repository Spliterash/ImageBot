package ru.spliterash.imageBot.domain.def;

import ru.spliterash.imageBot.domain.def.params.CaseParams;

public interface CaseExecutor {
    <C extends ImageCase<P>, P extends CaseParams> CaseIO execute(C c, CaseIO io, P values);
}
