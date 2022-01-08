package ru.spliterash.imageBot.realization.image.python.scanner;

import lombok.RequiredArgsConstructor;
import ru.spliterash.imageBot.domain.cases.ScanImageUseCase;
import ru.spliterash.imageBot.domain.def.params.EmptyParams;
import ru.spliterash.imageBot.domain.entities.ImageData;
import ru.spliterash.imageBot.realization.image.python.service.ExternalScriptService;

@RequiredArgsConstructor
public class PythonScannerCase extends ScanImageUseCase {
    private final ExternalScriptService externalScriptService;

    @Override
    public ImageData process(ImageData image, EmptyParams params) throws Exception {
        return null;
    }
}
