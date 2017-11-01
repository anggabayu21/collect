/*
 * Copyright (C) 2011 Angga Bayu angga.bayu.marthafifsa@gmail.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.gic.collect.android.logic;

import java.io.Serializable;

public class FormResetPasswordDetails implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public String errorStr;

    public String csrf;
    public String email;


    public FormResetPasswordDetails() {
        this.csrf = null;
        this.email = null;
        this.errorStr = null;
    }

    public FormResetPasswordDetails(String error) {
        this.csrf = null;
        this.email = null;
        this.errorStr = error;
    }

    public FormResetPasswordDetails(String csrf, String email) {
        this.csrf = csrf;
        this.email = email;
        this.errorStr = null;
    }

    public void setErrorStr(String errorStr){
        this.errorStr = errorStr;
    }

    public String getErrorStr(){
        return this.errorStr;
    }

}
