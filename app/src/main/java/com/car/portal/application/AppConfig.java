package com.car.portal.application;

import android.os.Environment;

public class AppConfig {


    public final static String UserSourceImg = Environment.getExternalStorageDirectory()+"/QBC/";
    public final static String WX_QR_CODE_URL = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=";
    public static final String APP_ID = "wxfb48eeb1c1737aaf";

    /*public static final String cer = "-----BEGIN CERTIFICATE-----\n" +
            "MIIDdzCCAl+gAwIBAgIEAgAAuTANBgkqhkiG9w0BAQUFADBaMQswCQYDVQQGEwJJRTESMBAGA1UE\n" +
            "ChMJQmFsdGltb3JlMRMwEQYDVQQLEwpDeWJlclRydXN0MSIwIAYDVQQDExlCYWx0aW1vcmUgQ3li\n" +
            "ZXJUcnVzdCBSb290MB4XDTAwMDUxMjE4NDYwMFoXDTI1MDUxMjIzNTkwMFowWjELMAkGA1UEBhMC\n" +
            "SUUxEjAQBgNVBAoTCUJhbHRpbW9yZTETMBEGA1UECxMKQ3liZXJUcnVzdDEiMCAGA1UEAxMZQmFs\n" +
            "dGltb3JlIEN5YmVyVHJ1c3QgUm9vdDCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAKME\n" +
            "uyKrmD1X6CZymrV51Cni4eiVgLGw41uOKymaZN+hXe2wCQVt2yguzmKiYv60iNoS6zjrIZ3AQSsB\n" +
            "UnuId9Mcj8e6uYi1agnnc+gRQKfRzMpijS3ljwumUNKoUMMo6vWrJYeKmpYcqWe4PwzV9/lSEy/C\n" +
            "G9VwcPCPwBLKBsua4dnKM3p31vjsufFoREJIE9LAwqSuXmD+tqYF/LTdB1kC1FkYmGP1pWPgkAx9\n" +
            "XbIGevOF6uvUA65ehD5f/xXtabz5OTZydc93Uk3zyZAsuT3lySNTPx8kmCFcB5kpvcY67Oduhjpr\n" +
            "l3RjM71oGDHweI12v/yejl0qhqdNkNwnGjkCAwEAAaNFMEMwHQYDVR0OBBYEFOWdWTCCR1jMrPoI\n" +
            "VDaGezq1BE3wMBIGA1UdEwEB/wQIMAYBAf8CAQMwDgYDVR0PAQH/BAQDAgEGMA0GCSqGSIb3DQEB\n" +
            "BQUAA4IBAQCFDF2O5G9RaEIFoN27TyclhAO992T9Ldcw46QQF+vaKSm2eT929hkTI7gQCvlYpNRh\n" +
            "cL0EYWoSihfVCr3FvDB81ukMJY2GQE/szKN+OMY3EU/t3WgxjkzSswF07r51XgdIGn9w/xZchMB5\n" +
            "hbgF/X++ZRGjD8ACtPhSNzkE1akxehi/oCr0Epn3o0WC4zxe9Z2etciefC7IpJ5OCBRLbf1wbWsa\n" +
            "Y71k5h+3zvDyny67G7fyUIhzksLi4xaNmjICq44Y3ekQEe5+NauQrz4wlHrQMz2nZQ/1/I6eYs9H\n" +
            "RCwBXbsdtTLSR9I4LtD+gdwyah617jzV/OeBHRnDJELqYzmp\n" +
            "-----END CERTIFICATE-----";*/
    public static final String cer = "-----BEGIN CERTIFICATE-----\n" +
            "MIIFkjCCBHqgAwIBAgIQDAlz3K5pO/NRRF9EMxCgWTANBgkqhkiG9w0BAQsFADBy\n" +
            "MQswCQYDVQQGEwJDTjElMCMGA1UEChMcVHJ1c3RBc2lhIFRlY2hub2xvZ2llcywg\n" +
            "SW5jLjEdMBsGA1UECxMURG9tYWluIFZhbGlkYXRlZCBTU0wxHTAbBgNVBAMTFFRy\n" +
            "dXN0QXNpYSBUTFMgUlNBIENBMB4XDTE4MDgyNzAwMDAwMFoXDTE5MDgyNzEyMDAw\n" +
            "MFowGDEWMBQGA1UEAxMNd3d3LjU2aG1jLmNvbTCCASIwDQYJKoZIhvcNAQEBBQAD\n" +
            "ggEPADCCAQoCggEBAKdLl6klKFPxDSyvgQEMGEvwzPYvoGAvy5bIGb7FEU3Nr5FM\n" +
            "XcaPSzAENtjcSxaUhis7MsXDnHH6jRikWO1pt6Ft6SA+Y+z9Q2cC4wA04y8fwEFv\n" +
            "QnQuIcz1+RkrI9lYWzUZGxNg071oufHQ0US1wHGjETUZtYNZRNvRBRKsZXy1vqMQ\n" +
            "b9Aj7CPwobZudVvPO347AhcNwQJacGv7Lm3YCCqL+pEqDoHxQuEnV4IunKpMOAMn\n" +
            "WEYQhCKEhkoWizUACgFptzehOnTyec/LXivloSTS+UMRdat9YLRczpnXWxwfsxLD\n" +
            "dyANOl0UQ15z2rXLdNFG4Kbnn8VkIrrlK5Fl/+UCAwEAAaOCAnwwggJ4MB8GA1Ud\n" +
            "IwQYMBaAFH/TmfOgRw4xAFZWIo63zJ7dygGKMB0GA1UdDgQWBBTMZyoiXTLSyz2L\n" +
            "B4tgVA4k+TImLzAjBgNVHREEHDAagg13d3cuNTZobWMuY29tggk1NmhtYy5jb20w\n" +
            "DgYDVR0PAQH/BAQDAgWgMB0GA1UdJQQWMBQGCCsGAQUFBwMBBggrBgEFBQcDAjBM\n" +
            "BgNVHSAERTBDMDcGCWCGSAGG/WwBAjAqMCgGCCsGAQUFBwIBFhxodHRwczovL3d3\n" +
            "dy5kaWdpY2VydC5jb20vQ1BTMAgGBmeBDAECATCBgQYIKwYBBQUHAQEEdTBzMCUG\n" +
            "CCsGAQUFBzABhhlodHRwOi8vb2NzcDIuZGlnaWNlcnQuY29tMEoGCCsGAQUFBzAC\n" +
            "hj5odHRwOi8vY2FjZXJ0cy5kaWdpdGFsY2VydHZhbGlkYXRpb24uY29tL1RydXN0\n" +
            "QXNpYVRMU1JTQUNBLmNydDAJBgNVHRMEAjAAMIIBAwYKKwYBBAHWeQIEAgSB9ASB\n" +
            "8QDvAHYApLkJkLQYWBSHuxOizGdwCjw1mAT5G9+443fNDsgN3BAAAAFleW6ieAAA\n" +
            "BAMARzBFAiEA9Q8MzXFh+rSdOBuIERNSm7HZ6KLWj2CQHaNmWpY4JL8CIEspamI8\n" +
            "QqPyc1sOzw+P9SjGPHLpFWUVC/k6d3t68nL6AHUAh3W/51l8+IxDmV+9827/Vo1H\n" +
            "Vjb/SrVgwbTq/16ggw8AAAFleW6jZQAABAMARjBEAh9aM6ROX3uoOvReD7EQ5cxK\n" +
            "yQx9Sw17WU5ccT2/vUGaAiEApCK3sKi/tSG+GkE9jPKks09SrfY/B2ql+cSlEl83\n" +
            "D4UwDQYJKoZIhvcNAQELBQADggEBAJY1wKDs2a/drHxeKEYjgOdB1hcf8A+/jhrZ\n" +
            "yIEqbfNqfeyTaQa4f4lp7wpCskYO2m4/TDUspSHk1g10Yk7g45RPt7AoDcutywbS\n" +
            "Knfi7M0EJlC7Lcoz1pZQvbTsR/f/TA/4ZMASNexcLz6u5GxaN+jfh6JJHQh7vZXF\n" +
            "qPgHZwoNx8iBZ8n+4riHfxbykSsynNeKsh9oKh1rxSS+UnX6npxGgQAA4CtrJRnw\n" +
            "aCJBL3LTtC2hlBceXABqRFGufO+n9boRCyWCIFmKNaWmVrGtDx3tbS+JVln9cjps\n" +
            "SYBNJL13fgoi+EjkazJLPiBMcD6wxfeid4kzQq12mcWgwn5zNzg=\n" +
            "-----END CERTIFICATE-----";
    //证书有效期为一年，到期前APP需要更新

    public static final String cerTest = "-----BEGIN CERTIFICATE-----\n" +
            "MIIDqzCCApOgAwIBAgIEdvHIOzANBgkqhkiG9w0BAQsFADCBhTELMAkGA1UEBhMCY24xEjAQBgNV\n" +
            "BAgMCeW5v+S4nOecgTEPMA0GA1UEBwwG5rex5ZyzMS0wKwYDVQQKDCTkuK3np5Hmmbrov5Dnianm\n" +
            "tYHnp5HmioDmnInpmZDlhazlj7gxFTATBgNVBAsMDOS4reenkeaZuui/kDELMAkGA1UEAxMCbGkw\n" +
            "HhcNMTUwNTIzMTA1MjM3WhcNMTUwODIxMTA1MjM3WjCBhTELMAkGA1UEBhMCY24xEjAQBgNVBAgM\n" +
            "CeW5v+S4nOecgTEPMA0GA1UEBwwG5rex5ZyzMS0wKwYDVQQKDCTkuK3np5Hmmbrov5DnianmtYHn\n" +
            "p5HmioDmnInpmZDlhazlj7gxFTATBgNVBAsMDOS4reenkeaZuui/kDELMAkGA1UEAxMCbGkwggEi\n" +
            "MA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCT8anXPdkjPNir4xnnK7e+Ndc6hR3am0TYKo5g\n" +
            "KWs0DYlaA7/SGZhoR8mVPR6FXdPHMEihs50K7K5nmZSIBsNXAU5ieTPqUTXzvuUsFVSoebhF9K8A\n" +
            "6cDb0eUI6kqu+hw83NaBosavSWhLrjW7JLnKQO0k4WG/nBYYW7H8Zp1CONDsdPJ+qbYMXfbkW4Ve\n" +
            "NZYCiYHwiqYVsD3m7VkAQ/xW7G9IuES++NPmtykDXHnaf/Ky0KrgZmgj9QxEhNO/uBc5jyhH9C8w\n" +
            "cJouhw7gBWr+x+5ewQnf9qd06UElUMF2Ib6i4ibB0w7loaC6BLRAL9F6MCuBNo1/rgtSzTxqJR1L\n" +
            "AgMBAAGjITAfMB0GA1UdDgQWBBSUvnTGafIZM/u187xG0BFifBFqQjANBgkqhkiG9w0BAQsFAAOC\n" +
            "AQEAdP0qew25zGfI7DsyT7uRXir0XxBOHgUxJ6o5cG3G5AYTI9Nvcckxy9/K7g0JXFP8LLmoOCDY\n" +
            "Mr+noU+cY7qEMmsissoaH6Rmicfs0bSPR7t8bPK9ctTIPgX/rA5FR/yLmwPBvEBFQkexKdag74Sx\n" +
            "DeyIV2XeASAy0q0tkBvXvNhmTjuN9//zLMkDMHzvAI48bzbe/tmisU8qSruM+1ooom2KPF5lUj9+\n" +
            "yAHfdV0Xe7nieufYxQdlK+09ek0jUa5ErQMPestGemiZ4gBozdB47S4nEa3q3mWjwa8uc4Am9stY\n" +
            "i1qAfdYfaknhH/1qp6IdcXeCelrg48JCL8VuuP+HFg==\n" +
            "-----END CERTIFICATE-----";
}
