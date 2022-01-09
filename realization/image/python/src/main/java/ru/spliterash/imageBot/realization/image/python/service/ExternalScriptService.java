package ru.spliterash.imageBot.realization.image.python.service;

import lombok.RequiredArgsConstructor;
import ru.spliterash.imageBot.domain.def.bean.Bean;
import ru.spliterash.imageBot.domain.entities.ImageData;
import ru.spliterash.imageBot.domain.utils.ImageUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class ExternalScriptService implements Bean {
    private final Set<Process> processes = new HashSet<>();

    public void execute(String programName, List<String> args) {
        Process process = null;
        try {
            ProcessBuilder builder = new ProcessBuilder(
                    Stream.concat(
                            Stream.of(programName),
                            args.stream()
                    ).collect(Collectors.toList())
            );
            process = builder.start();
            processes.add(process);

            process.waitFor();
        } catch (IOException | InterruptedException exception) {
            if (process != null) {
                processes.remove(process);
                process.destroyForcibly();
            }
        }
    }

    public void executePython(List<String> args) {
        execute("python", args);
    }

    public String toPath(ImageData data) {
        File file = ImageUtils.getImageFile(data);

        return file.getAbsolutePath();
    }


    @Override
    public void preDestroy() throws Exception {
        for (Process process : processes) {
            process.destroyForcibly();
        }
    }
}
