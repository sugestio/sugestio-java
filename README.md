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

# Tutorial and sample code

This tutorial first explains how you can use the basic functions of the <code>SugestioClient</code> to easily integrate the recommendation service into your existing work flow. In the advanced tutorial, we create a new application from scratch that leverages the more advanced, multi-threaded functions of the client to quickly import a large data set.

## Basic tutorial
For this basic tutorial, we use the example of a library that wants to recommend books to their members. The library already has an IT system for managing their book catalog and their member database. In this tutorial, we add recommendation service calls at various points in the existing work flow. Whenever a new book is added to the catalog, we submit its metadata to the recommendation service. We also provide the service with some demographic information on our members, such as age and gender, that can be helpful in determining what kind of books they like. Finally, we log which books are loaned by each member, so the service can get to know their actual tastes.

We start by creating a new instance of the <code>SugestioClient</code> class. For this basic tutorial, we use the constructor that takes two arguments -- the account key and the secret key.

	SugestioClient client = new SugestioClient("sandbox", "demo");

Now we are ready to submit data to the service. Let's begin by submitting the metadata of a book. The <code>Item</code> constructor takes a single argument -- a unique identifier for this book. Generally speaking, if you use a relational database system such as MySQL to store your application data, this identifier can be the auto-generated primary key from your Items table. In the specific case of our library application, the ISBN number of the book is a perfect candidate for a unique identifier.

	Item book = new Item("0151446474");

Additional information like keywords, author and genre can all be useful when the recommendation service tries to determine whether this book will match a person's interests.

	book.addTag("history");
	book.addTag("murder");
	book.addTag("mystery");
	book.addTag("whodunnit");	
	book.addCreator("Eco, Umberto");	
	book.addCategory("Fiction");

Finally, we submit the book to the service and we shut down the client library.

	client.addItem(book);
	client.shutdown();

We also keep some basic demographic information on our members. Like the <code>Item</code> class from before, the <code>User</code> constructor takes a single argument -- a unique identifier for this library member. Here we use a numerical ID that might have been automatically generated in the SQL backend of the library IT system. For the Gender field, we can make use of an Enumeration type.	
	
	User member = new User("1407");
	member.setGender(User.Gender.M);
	member.setBirthday(1975, 7, 12);
	
	SugestioClient client = new SugestioClient("sandbox", "demo");
	client.addUser(member);
	client.shutdown();

Finally, we keep a record of loans. This type of object takes two arguments in its constructor, the member ID and the book ID. Let's say user 1407 is checking out "The Name of the Rose" which had ISBN 0151446474:	
	
	Consumption loan = new Consumption("1407", "0151446474");

	SugestioClient client = new SugestioClient("sandbox", "demo");
	client.addConsumption(loan);
	client.shutdown();

Recommending new books to user 1407 is easy. Each recommendation object has an Item ID associated with it. In our case, this ID is the ISBN number. Let's assume our library application has a method <code>getBook()</code> which looks up a book based on its ISBN number.

	SugestioClient client = new SugestioClient("sandbox", "demo");
	List<Recommendation> recommendations = client.getRecommendations("1407");

	printLine("You might also like...");
	
	for (Recommendation recommendation : recommendations) {
		Book book = getBook(recommendation.getItemID());
		printLine(book.getTitle());
	}

	client.shutdown();

## Advanced tutorial

To do