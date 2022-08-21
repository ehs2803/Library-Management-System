package com.ehs.library.base.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
//public class AWSConfig {
//
//    private String iamAccessKey =
//    private String iamSecretKey =
//    private String region = "ap-northeast-2"; // Bucket Region
//
//    @Bean
//    public AmazonS3Client amazonS3Client() {
//        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(iamAccessKey, iamSecretKey);
//        return (AmazonS3Client) AmazonS3ClientBuilder.standard()
//                .withRegion(region)
//                .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
//                .build();
//    }
//}