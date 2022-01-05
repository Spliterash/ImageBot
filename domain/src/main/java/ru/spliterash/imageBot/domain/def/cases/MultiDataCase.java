package ru.spliterash.imageBot.domain.def.cases;

import net.jodah.typetools.TypeResolver;
import ru.spliterash.imageBot.domain.def.CaseIO;
import ru.spliterash.imageBot.domain.def.ICase;
import ru.spliterash.imageBot.domain.def.params.CaseParams;
import ru.spliterash.imageBot.domain.entities.Data;
import ru.spliterash.imageBot.domain.exceptions.ImageReadError;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class MultiDataCase<P extends CaseParams, ID extends Data, OD extends Data> implements ICase<P> {
    @Override
    public final CaseIO execute(CaseIO io, P params) throws ImageReadError {
        //noinspection unchecked
        Class<ID> dataClazz = (Class<ID>) TypeResolver.resolveRawArguments(SingleDataCase.class, getClass())[1];
        CaseIO.Container<ID> data = io.get(dataClazz);
        OD outputData = process(data.getNeedData(), params);

        return new CaseIO(Stream.concat(
                        Stream.of(outputData),
                        data.getRestData().stream()
                )
                .collect(Collectors.toList()));
    }

    public abstract OD process(List<ID> list, P params);
}
