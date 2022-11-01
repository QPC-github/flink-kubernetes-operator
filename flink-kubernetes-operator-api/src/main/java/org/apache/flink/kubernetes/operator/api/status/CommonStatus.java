/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.flink.kubernetes.operator.api.status;

import org.apache.flink.annotation.Experimental;
import org.apache.flink.kubernetes.operator.api.spec.AbstractFlinkSpec;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/** Last observed common status of the Flink deployment/Flink SessionJob. */
@Experimental
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class CommonStatus<SPEC extends AbstractFlinkSpec> {

    /** Last observed status of the Flink job on Application/Session cluster. */
    private JobStatus jobStatus = new JobStatus();

    /** Error information about the FlinkDeployment/FlinkSessionJob. */
    private String error = "";

    /**
     * Current reconciliation status of this resource.
     *
     * @return Current {@link ReconciliationStatus}.
     */
    public abstract ReconciliationStatus<SPEC> getReconciliationStatus();
}