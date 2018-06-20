package com.perceptnet.tools.codegen;

import com.perceptnet.commons.utils.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 * created by vkorovkin on 20.06.2018
 */
public class JavaSrcGenerationHelper {

    public <P> void generateJavaSrcFile(BaseGenerationContext ctx, String qualifiedClassName, BaseGenerator<P> generator, P generationParams) {
        generateFile(ctx, getSourceFileNameForClass(ctx, qualifiedClassName), generator, generationParams);
    }

    public <P> void generateFile(BaseGenerationContext ctx, String fileNameToBeGenerated, BaseGenerator<P> generator, P generationParams) {
        FileUtils.prepareFileForReCreation(fileNameToBeGenerated);
        try (PrintStream ps = new PrintStream(new FileOutputStream(fileNameToBeGenerated), true, ctx.getEncoding())) {
            generator.setOut(ps);
            generator.generate(generationParams);
            ps.flush();
        } catch (IOException e) {
            throw new RuntimeException("Cannot generate '" + fileNameToBeGenerated + "' due to " + e, e);
        }
    }

    public String getSourceFileNameForClass(BaseGenerationContext ctx, String qualifiedClassName) {
        String result = qualifiedClassName.replace(".", "/") + ".java";
        String baseOutputDir = ctx.getBaseOutputDir();
        if (baseOutputDir != null && !baseOutputDir.isEmpty()) {
            if (!baseOutputDir.endsWith("/") && !baseOutputDir.endsWith("\\") && !baseOutputDir.endsWith(File.separator)) {
                result = baseOutputDir + File.separator + result;
            } else {
                result = baseOutputDir + result;
            }
        }

        return result;
    }
}
