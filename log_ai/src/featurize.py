from sklearn.feature_extraction.text import TfidfVectorizer
import numpy as np

def build_tfidf(corpus, max_features=10000):
    vec = TfidfVectorizer(max_features=max_features, ngram_range=(1,2))
    X = vec.fit_transform(corpus)
    return vec, X

def transform_tfidf(vec, corpus):
    return vec.transform(corpus)
