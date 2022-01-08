package ru.spliterash.imageBot.domain.def.cases;

import net.jodah.typetools.TypeResolver;
import ru.spliterash.imageBot.domain.def.CaseIO;
import ru.spliterash.imageBot.domain.def.PipelineCase;
import ru.spliterash.imageBot.domain.def.params.CaseParams;
import ru.spliterash.imageBot.domain.entities.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Кейс работающий в режиме вход выход
 *
 * @param <P>
 * @param <ID>
 * @param <OD>
 */
public abstract class SingleDataCase<P extends CaseParams, ID extends Data, OD extends Data> implements PipelineCase<P> {
    @Override
    public final CaseIO execute(CaseIO io, P params) throws Exception {
        validate(params);
        //noinspection unchecked
        Class<ID> dataClazz = (Class<ID>) TypeResolver.resolveRawArguments(SingleDataCase.class, getClass())[1];

        CaseIO.Container<ID> data = io.get(dataClazz);

        List<Data> output = new ArrayList<>();

        for (ID needDatum : data.getNeedData()) {
            OD outputData = process(needDatum, params);

            output.add(outputData);
        }

        output.addAll(data.getRestData());

        return new CaseIO(output);
    }

    protected void validate(P params) {
        // default empty
    }


    public abstract OD process(ID image, P params) throws Exception;
}
