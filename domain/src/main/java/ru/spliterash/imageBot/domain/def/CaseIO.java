package ru.spliterash.imageBot.domain.def;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.spliterash.imageBot.domain.def.values.CaseValue;
import ru.spliterash.imageBot.domain.def.values.ImageValue;
import ru.spliterash.imageBot.domain.entities.DomainImage;
import ru.spliterash.imageBot.domain.exceptions.CaseErrorException;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public final class CaseIO {
    private final List<CaseValue> values;

    public DomainImage firstImage() {
        return values
                .stream()
                .filter(v -> v instanceof ImageValue)
                .findFirst()
                .map(v -> ((ImageValue) v).getImage())
                .orElseThrow(() -> new CaseErrorException("Нет картинки в выходных значениях кейса"));
    }

    public List<DomainImage> images() {
        return values.stream()
                .filter(v -> v instanceof ImageValue)
                .map(v -> ((ImageValue) v).getImage())
                .collect(Collectors.toList());
    }

}
