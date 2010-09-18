# Overview

This is a Java library for interfacing with the [Sugestio](http://www.sugestio.com) 
recommendation service. Data is submitted as XML. Users, items and consumptions can
be transmitted in bulk. The library makes use of the 
[Concurrency API](http://download.oracle.com/javase/1.5.0/docs/guide/concurrency/index.html) 
and the [Jersey](http://jersey.dev.java.net) RESTful web service client.

## About Sugestio

Sugestio is a scalable and fault tolerant service that now brings the power of 
web personalisation to all developers. The RESTful web service provides an easy to use 
interface and a set of developer libraries that enable you to enrich 
your content portals, E-Commerce sites and other content based websites.

### Access credentials and the Sandbox

To access the Sugestio service, you need an account name and a secret key. 
To run the examples from the tutorial, you can use the following credentials:

* account name: <code>sandbox</code>
* secret key: <code>demo</code>

The Sandbox is a *read-only* account. You can use these credentials to experiment 
with the service. The Sandbox can give personal recommendations for users 1 through 5, 
and similar items for items 1 through 5.

When you are ready to work with real data, you may apply for a developer account through 
the [Sugestio website](http://www.sugestio.com).  

## About this library

### Features

The following [API](http://www.sugestio.com/documentation) features are implemented:

* get personalized recommendations for a given user
* get items that are similar to a given item
* (bulk) submit user activity (consumptions): clicks, purchases, ratings, ...
* (bulk) submit item metadata: description, location, tags, categories, ...  	
* (bulk) submit user metadata: gender, location, birthday, ...
* get performance data (analytics): precision, recall, ...

### Requirements

This library is based on [Jersey](http://jersey.dev.java.net) -- an Open Source implementation of the JAX-RS 1.1 (JSR 311) API for building RESTful web services. The most recent version of the client requires Jersey 1.1.5.1 and the Jersey OAuth filter.

