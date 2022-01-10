package ru.spliterash.imageBot.realization.image.imageIO.cases;

import ru.spliterash.imageBot.domain.cases.CoverImageUseCase;
import ru.spliterash.imageBot.domain.def.CaseExecutor;
import ru.spliterash.imageBot.domain.def.executors.DefaultCaseExecutor;
import ru.spliterash.imageBot.domain.entities.ImageData;
import ru.spliterash.imageBot.realization.image.imageIO.utils.ImageIOTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CoverCaseTest {
    //    @Test
    public void testGlueCase() {
        CaseExecutor executor = new DefaultCaseExecutor();
        ImageIOCoverImageCase coverCase = new ImageIOCoverImageCase(executor, new ImageIOProportionResizeCase());

        ImageData quadratic = ImageIOTestUtils.loadCats().get(0);

        ImageData imageData = coverCase.process(quadratic, CoverImageUseCase.Input.builder()
                .width(1600)
                .cutImage(false)
                .build());


        assertEquals(1600, imageData.getWidth());
    }
}
