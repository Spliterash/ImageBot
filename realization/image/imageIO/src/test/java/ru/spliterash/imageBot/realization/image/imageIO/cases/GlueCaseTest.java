package ru.spliterash.imageBot.realization.image.imageIO.cases;

import ru.spliterash.imageBot.domain.cases.ComicImageCase;
import ru.spliterash.imageBot.domain.def.CaseExecutor;
import ru.spliterash.imageBot.domain.def.executors.DefaultCaseExecutor;
import ru.spliterash.imageBot.domain.entities.ImageData;
import ru.spliterash.imageBot.realization.image.imageIO.cases.glue.ComicImageCaseImpl;
import ru.spliterash.imageBot.realization.image.imageIO.utils.ImageIOTestUtils;

import java.util.List;

public class GlueCaseTest {
//    @Test
    public void testGlueCase() {
        CaseExecutor executor = new DefaultCaseExecutor();
        ImageIOResizeCase resize = new ImageIOResizeCase();
        ComicImageCaseImpl caseImpl = new ComicImageCaseImpl(executor, resize, new ImageIOCoverImageCase(executor, resize));

        List<ImageData> cats = ImageIOTestUtils.loadCats();

        ImageData imageData = executor.execute(caseImpl, cats, ComicImageCase.Params.builder()
                .resizeMode(ComicImageCase.ResizeMode.COVER)
                .columns(4)
                .max(99999)
                .needBorder(true)
                .max(1024)
                .build());


        ImageIOTestUtils.saveImage(imageData, "glueTestResult.png");
    }
}
