/*
 * Copyright (C) 2011 University of Washington
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

public class FormRegisterUserDetails implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public String errorStr;

    public String csrf;
    public String username;
    public String email;
    public String password1;
    public String password2;
    public String first_name;


    public FormRegisterUserDetails() {
        this.csrf = null;
        this.username = null;
        this.email = null;
        this.password1 = null;
        this.password2 = null;
        this.first_name = null;
        this.errorStr = null;
    }

    public FormRegisterUserDetails(String error) {
        this.csrf = null;
        this.username = null;
        this.email = null;
        this.password1 = null;
        this.password2 = null;
        this.first_name = null;
        this.errorStr = error;
    }

    public FormRegisterUserDetails(String csrf, String username, String email, String password1, String password2, String first_name) {
        this.csrf = csrf;
        this.username = username;
        this.email = email;
        this.password1 = password1;
        this.password2 = password2;
        this.first_name = first_name;
        this.errorStr = null;
    }

    public void setErrorStr(String errorStr){
        this.errorStr = errorStr;
    }

    public String getErrorStr(){
        return this.errorStr;
    }

}
