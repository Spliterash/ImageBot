package ru.spliterash.imageBot.text;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.spliterash.imageBot.domain.cases.CoverImageUseCase;
import ru.spliterash.imageBot.domain.cases.GlueImagesCase;
import ru.spliterash.imageBot.domain.cases.SliceDataCase;
import ru.spliterash.imageBot.domain.pipeline.PipelineStep;
import ru.spliterash.imageBot.pipelines.text.TextPipelineGenerator;
import ru.spliterash.imageBot.pipelines.text.types.ClueParse;
import ru.spliterash.imageBot.pipelines.text.types.CoverParser;
import ru.spliterash.imageBot.pipelines.text.types.SliceParser;
import ru.spliterash.imageBot.pipelines.text.utils.ParseUtils;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TextGeneratorTests {

    private final TextPipelineGenerator textPipelineGenerator;

    public TextGeneratorTests() {
        ParseUtils parseUtils = new ParseUtils();
        textPipelineGenerator = new TextPipelineGenerator(Arrays.asList(
                new ClueParse(Mockito.mock(GlueImagesCase.class), parseUtils),
                new CoverParser(Mockito.mock(CoverImageUseCase.class), parseUtils),
                new SliceParser(Mockito.mock(SliceDataCase.class))
        ));
    }

    @Test
    public void test() {
        String cmd = "удалить :-1" + "\n" +
                "удалить :1" + "\n" +
                "склеить" + "\n" +
                "cover -w 12 -h 20";

        List<PipelineStep<?, ?>> list = textPipelineGenerator.parse(cmd);

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
