/*
 * Copyright 2016 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.chrisgahlert.gradledcomposeplugin.extension

import groovy.transform.TypeChecked
import org.gradle.util.GUtil

@TypeChecked
abstract class Service {
    /**
     * The internal dcompose service name, e.g. "database"
     */
    final String name

    /**
     * The path to the Gradle project where this service is defined
     */
    final String projectPath

    Service(String name, String projectPath) {
        this.name = name
        this.projectPath = projectPath
    }

    String getPullImageTaskName() {
        "pull${getTaskLabel()}Image"
    }

    @Deprecated
    String getPullTaskName() {
        pullImageTaskName
    }

    String getCreateContainerTaskName() {
        "create${getTaskLabel()}Container"
    }

    @Deprecated
    String getCreateTaskName() {
        createContainerTaskName
    }

    String getStartContainerTaskName() {
        "start${getTaskLabel()}Container"
    }

    @Deprecated
    String getStartTaskName() {
        startContainerTaskName
    }

    String getStopContainerTaskName() {
        "stop${getTaskLabel()}Container"
    }

    @Deprecated
    String getStopTaskName() {
        stopContainerTaskName
    }

    String getRemoveContainerTaskName() {
        "remove${getTaskLabel()}Container"
    }

    String getRemoveImageTaskName() {
        "remove${getTaskLabel()}Image"
    }

    String getBuildImageTaskName() {
        "build${getTaskLabel()}Image"
    }

    @Deprecated
    String getBuildTaskName() {
        buildImageTaskName
    }

    private String getTaskLabel() {
        GUtil.toCamelCase(name)
    }

    @Override
    String toString() {
        containerName
    }

    abstract String getContainerName()

    abstract boolean isWaitForCommand()

    abstract boolean isIgnoreExitCode()

    abstract int getWaitTimeout()

    abstract boolean isPreserveVolumes()

    abstract List<String> getCommand()

    abstract List<String> getEntrypoints()

    abstract List<String> getEnv()

    abstract String getWorkingDir()

    abstract String getUser()

    abstract Boolean getReadonlyRootfs()

    abstract List<String> getVolumes()

    abstract List<String> getBinds()

    abstract List getVolumesFrom()

    abstract List<String> getExposedPorts()

    abstract List<String> getPortBindings()

    abstract Boolean getPublishAllPorts()

    abstract List getLinks()

    abstract String getHostName()

    abstract List<String> getDns()

    abstract List<String> getDnsSearch()

    abstract List<String> getExtraHosts()

    abstract String getNetworkMode()

    abstract Boolean getAttachStdin()

    abstract Boolean getAttachStdout()

    abstract Boolean getAttachStderr()

    abstract Boolean getPrivileged()

    abstract File getBaseDir()

    abstract String getDockerFilename()

    abstract Long getMemory()

    abstract Long getMemswap()

    abstract String getCpushares()

    abstract String getCpusetcpus()

    abstract Map<String, String> getBuildArgs()

    abstract Boolean getForceRemoveImage()

    abstract Boolean getNoPruneParentImages()

    abstract Boolean getBuildNoCache()

    abstract Boolean getBuildRemove()

    abstract Boolean getBuildPull()

    abstract void setHostPortBindings(Map hostPortBindings)

    abstract def findHostPort(Map<String, String> properties, int containerPort)

    abstract void setDockerHost(URI uri);

    abstract String getDockerHost();

    abstract Set<Service> getLinkDependencies()

    abstract Set<Service> getVolumesFromDependencies()

    abstract String getTag()

    abstract String getImage()

    abstract boolean hasImage()

    abstract void validate()

    ServiceDependency link(String alias = null) {
        new ServiceDependency({ "$containerName:${alias ?: name}" }, this)
    }

    static class ServiceDependency {
        final Service service
        final private Closure definitionAction

        ServiceDependency(Closure definitionAction, Service service = null) {
            this.service = service
            this.definitionAction = definitionAction
        }

        String getDefinition() {
            definitionAction() as String
        }

        @Override
        String toString() {
            getDefinition()
        }
    }

    @Override
    boolean equals(Object obj) {
        if(obj == null || !(obj instanceof Service)) {
            return false
        }

        def other = (Service) obj
        return Objects.equals(this.name, other.name) && Objects.equals(this.projectPath, other.projectPath)
    }

    @Override
    int hashCode() {
        return Objects.hash(name, projectPath)
    }
}