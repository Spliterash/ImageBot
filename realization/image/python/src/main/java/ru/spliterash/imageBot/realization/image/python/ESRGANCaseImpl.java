package ru.spliterash.imageBot.realization.image.python;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import ru.spliterash.imageBot.domain.cases.ESRGANCase;
import ru.spliterash.imageBot.domain.entities.ImageData;
import ru.spliterash.imageBot.domain.entities.defaultEnt.FileImage;
import ru.spliterash.imageBot.domain.exceptions.WrongPipelineInputException;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ESRGANCaseImpl extends ESRGANCase {
    private final File esrganFolder = new File("esrgan");
    private final File inputFolder = new File(esrganFolder, "input");

    private final Set<Process> processes = new HashSet<>();

    private List<File> fileModels() {
        return Arrays.stream(Objects.requireNonNull(new File("esrgan" + File.separator + "models").listFiles(f -> f.getName().endsWith(".pth"))))
                .sorted(Comparator.comparing(File::getName))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> models() {
        List<File> files = fileModels();
        return IntStream.range(0, files.size())
                .mapToObj(i -> (i + 1) + ") " + files.get(i).getName())
                .collect(Collectors.toList());
    }

    @Override
    public ImageData process(ImageData image, Params params) throws Exception {
        String fileName = ThreadLocalRandom.current().nextInt() + ".png";
        File inputFile = new File(esrganFolder, "input" + File.separator + fileName);

        try (FileOutputStream stream = new FileOutputStream(inputFile, false)) {
            IOUtils.copy(image.read(), stream);
        }

        List<File> files = fileModels();
        if (params.getModel() >= files.size())
            throw new WrongPipelineInputException("Выбранной модели не существует", ESRGANCaseImpl.class);

        String modelName = files.get(params.getModel()).getName();

        ProcessBuilder builder = new ProcessBuilder("python", "upscale.py", "-c", modelName);

        builder.directory(esrganFolder);

        Process process = builder.start();
        processes.add(process);

        process.waitFor();

        inputFile.delete();

        File outputFile = new File(esrganFolder, "output" + File.separator + fileName);

        outputFile.deleteOnExit();

        return new FileImage(outputFile);
    }

    @Override
    public void postConstruct() throws Exception {

        FileUtils.deleteDirectory(inputFolder);
        inputFolder.mkdir();
    }

    @Override
    public void preDestroy() throws Exception {
        processes.forEach(Process::destroyForcibly);
    }
}
