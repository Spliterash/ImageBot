package ru.spliterash.imageBot.text;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.spliterash.imageBot.domain.cases.CoverImageUseCase;
import ru.spliterash.imageBot.domain.cases.ComicImageCase;
import ru.spliterash.imageBot.domain.cases.SliceDataCase;
import ru.spliterash.imageBot.domain.pipeline.PipelineStep;
import ru.spliterash.imageBot.pipelines.text.CLIPipelineGenerator;
import ru.spliterash.imageBot.pipelines.text.types.CoverCaseParser;
import ru.spliterash.imageBot.pipelines.text.types.ComicCaseParser;
import ru.spliterash.imageBot.pipelines.text.types.SliceCaseParser;
import ru.spliterash.imageBot.pipelines.text.utils.ParseUtils;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TextGeneratorTests {

    private final CLIPipelineGenerator CLIPipelineGenerator;

    public TextGeneratorTests() {
        ParseUtils parseUtils = new ParseUtils();
        CLIPipelineGenerator = new CLIPipelineGenerator(Arrays.asList(
                new ComicCaseParser(Mockito.mock(ComicImageCase.class), parseUtils),
                new CoverCaseParser(Mockito.mock(CoverImageUseCase.class), parseUtils),
                new SliceCaseParser(Mockito.mock(SliceDataCase.class))
        ));
    }

    @Test
    public void test() {
        String[] cmd = new String[]{
                "удалить :-1",
                "удалить :1",
                "склеить",
                "размер -w 12 -h 20"
        };

        List<PipelineStep<?, ?>> list = CLIPipelineGenerator.parse(cmd);

        assertEquals(4, list.size());

        {
            PipelineStep<?, ?> pipelineStep = list.get(0);
            assertTrue(SliceDataCase.Input.class.isAssignableFrom(pipelineStep.getParams().getClass()));
            SliceDataCase.Input input = (SliceDataCase.Input) pipelineStep.getParams();

            assertEquals(-1, input.getOperation());
            assertEquals(SliceDataCase.Input.Type.SLICE, input.getType());
        }

        {
            PipelineStep<?, ?> pipelineStep = list.get(3);
            assertTrue(CoverImageUseCase.Input.class.isAssignableFrom(pipelineStep.getParams().getClass()));
            CoverImageUseCase.Input input = (CoverImageUseCase.Input) pipelineStep.getParams();

            assertEquals(12, input.getWidth());
            assertEquals(20, input.getHeight());
        }
    }
}
