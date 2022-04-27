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

package org.springframework.boot.gradle.tasks.aot;

import java.util.ArrayList;
import java.util.List;

import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.JavaExec;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;

/**
 * Custom {@link JavaExec} task for generating sources ahead of time.
 *
 * @author Andy Wilkinson
 * @since 3.0
 */
public class GenerateAotSources extends JavaExec {

	private final Property<String> applicationClass;

	private final DirectoryProperty sourcesDir;

	private final DirectoryProperty resourcesDir;

	public GenerateAotSources() {
		this.applicationClass = getProject().getObjects().property(String.class);
		this.sourcesDir = getProject().getObjects().directoryProperty();
		this.resourcesDir = getProject().getObjects().directoryProperty();
		getMainClass().set("org.springframework.boot.AotProcessor");
	}

	@Input
	public Property<String> getApplicationClass() {
		return this.applicationClass;
	}

	@OutputDirectory
	public DirectoryProperty getSourcesDir() {
		return this.sourcesDir;
	}

	@OutputDirectory
	public DirectoryProperty getResourcesDir() {
		return this.resourcesDir;
	}

	@Override
	@TaskAction
	public void exec() {
		List<String> args = new ArrayList<>();
		args.add(this.applicationClass.get());
		args.add(this.sourcesDir.getAsFile().get().getAbsolutePath());
		args.add(this.resourcesDir.getAsFile().get().getAbsolutePath());
		args.addAll(super.getArgs());
		this.setArgs(args);
		super.exec();
	}

}
