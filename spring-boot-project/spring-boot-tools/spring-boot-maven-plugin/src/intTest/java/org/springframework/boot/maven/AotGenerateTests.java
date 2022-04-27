/*
 * Copyright 2012-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.maven;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for the Maven plugin's AOT support.
 *
 * @author Stephane Nicoll
 */
@ExtendWith(MavenBuildExtension.class)
public class AotGenerateTests {

	@TestTemplate
	void whenAotRunsSourcesAreGenerated(MavenBuild mavenBuild) {
		mavenBuild.project("aot").goals("package").execute((project) -> {
			Path aotDirectory = project.toPath().resolve("target/spring-aot/main");
			assertThat(collectRelativeFileNames(aotDirectory.resolve("sources")))
					.containsOnly("org/test/SampleApplication__ApplicationContextInitializer.java");
			assertThat(collectRelativeFileNames(aotDirectory.resolve("resources"))).containsOnly(
					"META-INF/native-image/reflect-config.json", "META-INF/native-image/native-image.properties");
		});
	}

	@TestTemplate
	void whenAotRunsSourcesAreCompiled(MavenBuild mavenBuild) {
		mavenBuild.project("aot").goals("package").execute((project) -> {
			Path classesDirectory = project.toPath().resolve("target/classes");
			assertThat(collectRelativeFileNames(classesDirectory))
					.contains("org/test/SampleApplication__ApplicationContextInitializer.class");
		});
	}

	@TestTemplate
	void whenAotRunsResourcesAreCopiedUsingProjectCoordinates(MavenBuild mavenBuild) {
		mavenBuild.project("aot").goals("package").execute((project) -> {
			Path classesDirectory = project.toPath().resolve("target/classes/META-INF/native-image");
			assertThat(collectRelativeFileNames(classesDirectory)).contains(
					"org.springframework.boot.maven.it/aot/reflect-config.json",
					"org.springframework.boot.maven.it/aot/native-image.properties");
		});
	}

	Stream<String> collectRelativeFileNames(Path sourceDirectory) {
		try {
			return Files.walk(sourceDirectory).filter(Files::isRegularFile)
					.map((path) -> path.subpath(sourceDirectory.getNameCount(), path.getNameCount()).toString());
		}
		catch (IOException ex) {
			throw new IllegalStateException(ex);
		}
	}

}
