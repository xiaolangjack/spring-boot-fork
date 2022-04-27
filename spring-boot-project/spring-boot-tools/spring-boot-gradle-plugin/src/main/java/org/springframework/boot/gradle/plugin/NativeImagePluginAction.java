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

package org.springframework.boot.gradle.plugin;

import java.util.List;

import org.graalvm.buildtools.gradle.NativeImagePlugin;
import org.graalvm.buildtools.gradle.dsl.GraalVMExtension;
import org.graalvm.buildtools.gradle.tasks.BuildNativeImageTask;
import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.TaskProvider;

import org.springframework.boot.gradle.tasks.aot.GenerateAotSources;

/**
 * {@link Action} that is executed in response to the {@link NativeImagePlugin} being
 * applied.
 *
 * @author Andy Wilkinson
 */
class NativeImagePluginAction implements PluginApplicationAction {

	@Override
	public Class<? extends Plugin<? extends Project>> getPluginClass()
			throws ClassNotFoundException, NoClassDefFoundError {
		return NativeImagePlugin.class;
	}

	@Override
	public void execute(Project project) {
		project.getPlugins().withType(JavaPlugin.class).all((plugin) -> {
			SourceSet aotSourceSet = configureAotSourceSet(project);
			registerGenerateAotSourcesTask(project, aotSourceSet);
			project.getTasks().named(NativeImagePlugin.NATIVE_COMPILE_TASK_NAME, BuildNativeImageTask.class,
					(nativeCompile) -> nativeCompile.getOptions().get().classpath(aotSourceSet.getOutput()));
		});
		GraalVMExtension graalVmExtension = project.getExtensions().getByType(GraalVMExtension.class);
		graalVmExtension.getToolchainDetection().set(false);
	}

	private SourceSet configureAotSourceSet(Project project) {
		JavaPluginExtension javaPluginExtension = project.getExtensions().getByType(JavaPluginExtension.class);
		SourceSetContainer sourceSets = javaPluginExtension.getSourceSets();
		SourceSet main = sourceSets.getByName(SourceSet.MAIN_SOURCE_SET_NAME);
		SourceSet aotSourceSet = sourceSets.create("aot", (aot) -> {
			aot.getJava().setSrcDirs(List.of("build/generated/aotSources"));
			aot.getResources().setSrcDirs(List.of("build/generated/aotResources"));
			aot.setCompileClasspath(aot.getCompileClasspath().plus(main.getOutput()));
			main.setRuntimeClasspath(main.getRuntimeClasspath().plus(aot.getOutput()));
			ConfigurationContainer configurations = project.getConfigurations();
			Configuration aotImplementation = configurations.getByName(aot.getImplementationConfigurationName());
			aotImplementation.extendsFrom(configurations.getByName(main.getImplementationConfigurationName()));
			aotImplementation.extendsFrom(configurations.getByName(main.getRuntimeOnlyConfigurationName()));
		});
		return aotSourceSet;
	}

	private void registerGenerateAotSourcesTask(Project project, SourceSet aotSourceSet) {
		TaskProvider<ResolveMainClassName> resolveMainClassName = project.getTasks()
				.named(SpringBootPlugin.RESOLVE_MAIN_CLASS_NAME_TASK_NAME, ResolveMainClassName.class);
		TaskProvider<GenerateAotSources> generateAotSources = project.getTasks().register("generateAotSources",
				GenerateAotSources.class, (task) -> {
					task.getApplicationClass().set(resolveMainClassName.flatMap((thing) -> thing.readMainClassName()));
					task.setClasspath(aotSourceSet.getCompileClasspath());
					task.getSourcesDir().set(aotSourceSet.getJava().getSrcDirs().iterator().next());
					task.getResourcesDir().set(aotSourceSet.getResources().getSrcDirs().iterator().next());
				});
		project.getTasks().getByName(aotSourceSet.getCompileJavaTaskName(),
				(compile) -> compile.dependsOn(generateAotSources));
		project.getTasks().getByName(aotSourceSet.getProcessResourcesTaskName(),
				(processResources) -> processResources.dependsOn(generateAotSources));
	}

}
