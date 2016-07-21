/*
 * Copyright (c) 2015 General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */

package com.ge.predix.solsvc.spi;

import java.util.Map;

/**
 * This interface provides a methods to create REST Web services Endpoint
 * 
 * @author predix
 */
public interface IServiceManagerService
{

    /**
     * @param arg0 -
     */
    void createRestWebService(Object arg0);

    /**
     * @param arg0 -
     * @param arg1 -
     */
    void createRestWebService(Object arg0, Map<String, String> arg1);

}
