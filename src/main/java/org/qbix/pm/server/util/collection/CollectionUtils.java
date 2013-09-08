package org.qbix.pm.server.util.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.qbix.pm.server.util.collection.predicates.AlwaysTruePredicate;
import org.qbix.pm.server.util.collection.predicates.Predicate;
import org.qbix.pm.server.util.collection.processors.EmptyProcessor;
import org.qbix.pm.server.util.collection.processors.Processor;
import org.qbix.pm.server.util.collection.returnfilters.NonChangingReturnFilter;
import org.qbix.pm.server.util.collection.returnfilters.ReturnFilter;

public class CollectionUtils {

	public static class Converter<T> {
		private final Collection<T> collection;

		private Converter(Collection<T> collection) {
			this.collection = collection;
		}

		public List<T> toList() {
			return new ArrayList<T>(collection);
		}

		public Set<T> toSet() {
			return new HashSet<T>(collection);
		}

		public T toEntity() throws EmptyCollectionException,
				ConversionException {
			if (collection.isEmpty()) {
				throw new EmptyCollectionException("the collection is empty");
			}
			if (collection.size() > 1) {
				throw new ConversionException(
						"unable to convert collection to a single entity, because the collection contains more than one");
			}
			return collection.iterator().next();
		}

	}

	public static class ConversionException extends RuntimeException {

		private static final long serialVersionUID = 5163293346058869964L;

		public ConversionException() {
			super();
		}

		public ConversionException(String message) {
			super(message);
		}
	}

	public static class EmptyCollectionException extends RuntimeException {
		
		private static final long serialVersionUID = -1611744765391170392L;

		public EmptyCollectionException() {
			super();
		}

		public EmptyCollectionException(String message) {
			super(message);
		}
	}

	@SafeVarargs
	public static <T> Converter<T> filterEntities(Collection<T> source,
			Processor<T> preprocessor, PredicateType predType,
			Predicate<T>... predicates) {
		return filterEntities(source, preprocessor, predType,
				new NonChangingReturnFilter<T>(), Arrays.asList(predicates));
	}

	public static <T, U> Converter<U> filterEntities(Collection<T> source,
			Processor<T> preprocessor, PredicateType predType,
			ReturnFilter<T, U> returnPredicate,
			Iterable<Predicate<T>> predicates) {
		Set<U> filtered = new HashSet<U>();
		if (preprocessor == null) {
			preprocessor = new EmptyProcessor<T>();
		}
		for (T entity : source) {
			T processedEntity = preprocessor.process(entity);
			boolean isSatisfied = true;
			for (Predicate<T> predicate : predicates) {
				isSatisfied = predType.combine(isSatisfied,
						predicate.isSatisfiedBy(processedEntity));
			}
			if (isSatisfied) {
				filtered.add(returnPredicate.returns(entity));
			}
		}
		return new Converter<U>(filtered);
	}

	@SafeVarargs
	public static <T> Converter<T> filterEntities(Collection<T> source,
			Processor<T> preprocessor, Predicate<T>... predicates) {
		return filterEntities(source, preprocessor, PredicateType.ALL,
				predicates);
	}

	public static <T> Converter<T> filterEntities(Collection<T> source,
			Processor<T> preprocessor, Iterable<Predicate<T>> predicates) {
		return filterEntities(source, preprocessor, PredicateType.ALL,
				new NonChangingReturnFilter<T>(), predicates);
	}

	@SafeVarargs
	public static <T> Converter<T> filterEntities(Collection<T> source,
			Predicate<T>... predicates) {
		return filterEntities(source, null, PredicateType.ALL, predicates);
	}

	public static <T, U> Converter<U> filterEntities(Collection<T> source,
			Predicate<T> predicate, ReturnFilter<T, U> returnPredicate) {
		if (predicate == null) {
			predicate = new AlwaysTruePredicate<T>();
		}
		Set<U> filtered = new HashSet<U>();
		for (T entity : source) {
			if (predicate.isSatisfiedBy(entity)) {
				filtered.add(returnPredicate.returns(entity));
			}
		}
		return new Converter<U>(filtered);
	}

	public static <T, U> Converter<U> filterEntities(Collection<T> source,
			ReturnFilter<T, U> returnPredicate) {
		return filterEntities(source, null, returnPredicate);
	}

	public static <K, V> Map<K, V> filterMap(Map<K, V> source,
			Predicate<Entry<K, V>> predicate) {
		Map<K, V> filtered = new HashMap<K, V>();
		for (Entry<K, V> entry : source.entrySet()) {
			if (predicate.isSatisfiedBy(entry)) {
				filtered.put(entry.getKey(), entry.getValue());
			}
		}
		return filtered;
	}
}
