package ru.spliterash.imageBot.domain.def;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.spliterash.imageBot.domain.entities.ImageData;

import java.util.Collections;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public final class ImageCaseContext {
    private List<ImageData> images = Collections.emptyList();

    public void set(List<ImageData> data) {
        this.images = List.copyOf(data);
    }
}
