package ru.spliterash.imageBot.domain.def.cases;

import net.jodah.typetools.TypeResolver;
import ru.spliterash.imageBot.domain.def.CaseIO;
import ru.spliterash.imageBot.domain.def.PipelineCase;
import ru.spliterash.imageBot.domain.def.params.CaseParams;
import ru.spliterash.imageBot.domain.entities.Data;
import ru.spliterash.imageBot.domain.exceptions.ImageReadError;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class MultiDataCase<P extends CaseParams, ID extends Data, OD extends Data> implements PipelineCase<P> {
    @Override
    public final CaseIO execute(CaseIO io, P params) throws ImageReadError {
        validate(params);
        //noinspection unchecked
        Class<ID> dataClazz = (Class<ID>) TypeResolver.resolveRawArguments(MultiDataCase.class, getClass())[1];
        CaseIO.Container<ID> data = io.get(dataClazz);
        if (data.getNeedData().size() == 0)
            return io; // Скипаем

        OD outputData = process(data.getNeedData(), params);

        return new CaseIO(Stream.concat(
                        Stream.of(outputData),
                        data.getRestData().stream()
                )
                .collect(Collectors.toList()));
    }

    protected void validate(P params) {

    }

    public abstract OD process(List<ID> list, P params);
}
