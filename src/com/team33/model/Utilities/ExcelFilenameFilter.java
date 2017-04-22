package com.team33.model.Utilities;

/**
 * Created by Amine on 17/02/2017.
 */

import java.io.File;
import java.io.FilenameFilter;

/**
 * An instance of this class can be used to control the files returned
 * be a call to the listFiles() method when made on an instance of the
 * File class and that object refers to a folder/directory
 */
class ExcelFilenameFilter implements FilenameFilter {

    /**
     * Determine those files that will be returned by a call to the
     * listFiles() method. In this case, the name of the file must end with
     * either of the following two extension; '.xls' or '.xlsx'. For the
     * future, it is very possible to parameterise this and allow the
     * containing class to pass, for example, an array of Strings to this
     * class on instantiation. Each element in that array could encapsulate
     * a valid file extension - '.xls', '.xlsx', '.xlt', '.xlst', etc. These
     * could then be used to control which files were returned by the call
     * to the listFiles() method.
     *
     * @param file An instance of the File class that encapsulates a handle
     *             referring to the folder/directory that contains the file.
     * @param name An instance of the String class that encapsulates the
     *             name of the file.
     * @return A boolean value that indicates whether the file should be
     *         included in the array retirned by the call to the listFiles()
     *         method. In this case true will be returned if the name of the
     *         file ends with either '.xls' or '.xlsx' and false will be
     *         returned in all other instances.
     */
    @Override
    public boolean accept(File file, String name) {
        return(name.endsWith(".xls") || name.endsWith(".xlsx"));
    }
}