package ru.spliterash.imageBot.domain.def;

import ru.spliterash.imageBot.domain.def.params.CaseParams;
import ru.spliterash.imageBot.domain.def.values.ImageValue;
import ru.spliterash.imageBot.domain.entities.DomainImage;
import ru.spliterash.imageBot.domain.exceptions.ImageReadError;

import java.util.List;
import java.util.stream.Collectors;

public interface ImageCase<P extends CaseParams> {
    CaseIO execute(CaseIO io, P params) throws ImageReadError;

    default CaseIO execute(DomainImage image, P params) {
        return execute(of(image), params);
    }

    default CaseIO execute(List<DomainImage> images, P params) {
        return execute(of(images), params);
    }

    default CaseIO of(List<DomainImage> images) {
        return new CaseIO(images.stream().map(ImageValue::new).collect(Collectors.toList()));
    }

    default CaseIO of(DomainImage image) {
        return new CaseIO(List.of(new ImageValue(image)));
    }
}
