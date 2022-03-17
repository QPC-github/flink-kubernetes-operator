---
title: "Guide"
weight: 1
type: docs
aliases:
- /development/guide.html
---
<!--
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.
-->

# Development Guide

We gathered a set of best practices here to aid development.

## Local environment setup

We recommend you install [Docker Desktop](https://www.docker.com/products/docker-desktop), [minikube](https://minikube.sigs.k8s.io/docs/start/)
and [helm](https://helm.sh/docs/intro/quickstart/) on your local machine. For the setup please refer to our 
[quickstart]({{< ref "docs/try-flink-kubernetes-operator/quick-start" >}}#prerequisites).

### Building docker images
You can build your own flavor of image as follows via specifying your `<repo>`:
```bash
docker build . -t <repo>/flink-java-operator:latest
docker push <repo>/flink-java-operator:latest
```

If you are using minikube you might want to load the image directly instead of pushing it to a registry:

```bash
minikube image load <repo>/flink-java-operator:latest
```

You can cut a corner via using the docker daemon of your minikube installation directly as follows:
```bash
eval $(minikube docker-env)
DOCKER_BUILDKIT=1 docker build . -t <repo>/flink-operator
```

When you want to reset your environment to the defaults you can do the following:
```bash
eval $(minikube docker-env --unset)
```

The most useful insight when it comes to minikube that it is just a docker container on your local machine and you can 
ssh to it with the following command in case you needed to hack something there (like adding a hostpath mount or modifying docker images).

```bash
minikube ssh
Last login: Wed Mar 9 10:01:21 2022 from 192.168.49.1
docker@minikube:~$ docker images
REPOSITORY                                             TAG                IMAGE ID       CREATED         SIZE
flink-operator                                         latest             cf7856d9ef59   23 hours ago    578MB
docker@minikube:~$ exit
```

### Installing the operator locally
```bash
helm install flink-operator helm/flink-operator --set image.repository=<repo> --set image.tag=latest
```
### Running the operator locally
You can run or debug the `FlinkOperator` from your preferred IDE. The operator itself is accessing the deployed Flink clusters through the REST interface. When running locally the `rest.port` and `rest.address` Flink configuration parameters must be modified to a locally accessible value.

When using `minikube tunnel` the rest service is exposed on `localhost:8081`
```bash
> minikube tunnel

> kubectl get services
NAME                         TYPE           CLUSTER-IP     EXTERNAL-IP   PORT(S)             AGE
basic-session-example        ClusterIP      None           <none>        6123/TCP,6124/TCP   14h
basic-session-example-rest   LoadBalancer   10.96.36.250   127.0.0.1     8081:30572/TCP      14h
```
The operator picks up the default log and flink configurations from `/opt/flink/conf`. You can put the rest configuration parameters here:
```bash
cat /opt/flink/conf/flink-conf.yaml
rest.port: 8081
rest.address: localhost
```

## CI/CD
We use [GitHub Actions](https://help.github.com/en/actions/getting-started-with-github-actions/about-github-actions) to help you automate your software development workflows in the same place you store code and collaborate on pull requests and issues.
You can write individual tasks, called actions, and combine them to create a custom workflow.
Workflows are custom automated processes that you can set up in your repository to build, test, package, release, or deploy any code project on GitHub.

Considering the cost of running the builds, the stability, and the maintainability, flink-kubernetes-operator chose GitHub Actions and build the whole CI/CD solution on it.
All the unit tests, integration tests, and the end-to-end tests will be triggered for each PR.

Note: Please make sure the CI passed before merging.