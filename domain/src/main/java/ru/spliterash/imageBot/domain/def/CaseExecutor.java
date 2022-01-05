package ru.spliterash.imageBot.domain.def;

import ru.spliterash.imageBot.domain.def.cases.SingleDataCase;
import ru.spliterash.imageBot.domain.def.params.CaseParams;
import ru.spliterash.imageBot.domain.entities.Data;

public interface CaseExecutor {
    <C extends ICase<P>, P extends CaseParams> CaseIO execute(C c, CaseIO io, P params);

    <C extends SingleDataCase<P, ID, OD>, P extends CaseParams, ID extends Data, OD extends Data> OD execute(C c, ID id, P params);
}
