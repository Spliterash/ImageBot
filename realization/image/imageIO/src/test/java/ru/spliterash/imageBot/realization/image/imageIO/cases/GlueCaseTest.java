package ru.spliterash.imageBot.realization.image.imageIO.cases;

import org.junit.jupiter.api.Test;
import ru.spliterash.imageBot.domain.cases.GlueImagesCase;
import ru.spliterash.imageBot.domain.def.CaseExecutor;
import ru.spliterash.imageBot.domain.def.executors.DefaultCaseExecutor;
import ru.spliterash.imageBot.domain.entities.ImageData;
import ru.spliterash.imageBot.realization.image.imageIO.utils.ImageIOTestUtils;

import java.util.List;

public class GlueCaseTest {
    @Test
    public void testGlueCase() {
        CaseExecutor executor = new DefaultCaseExecutor();
        ImageIOResizeCase resize = new ImageIOResizeCase();
        ImageIoGlueImageCaseImpl caseImpl = new ImageIoGlueImageCaseImpl(executor, resize, new ImageIOCoverImageCase(executor, resize));

        List<ImageData> cats = ImageIOTestUtils.loadCats();

        ImageData imageData = executor.execute(caseImpl, cats, GlueImagesCase.GlueImagesParams.builder()
                .resizeMode(GlueImagesCase.ResizeMode.COVER)
                .columns(4)
                .max(99999)
                .needBorder(true)
                .max(1024)
                .build());


        ImageIOTestUtils.saveImage(imageData, "result.png");
    }
}
