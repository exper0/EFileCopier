package com.github.exper0.efilecopier.ftp;

/**
 * Created by Acer-V3 on 06.01.2017.
 */
public interface FileAdapterFactory<T extends ReportSettings> {
    FileAdapter create(T settings);
}
