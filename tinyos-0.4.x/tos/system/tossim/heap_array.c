/*
 *   FILE: heap.h
 * AUTHOR: Philip Levis <pal@cs.berkeley.edu>
 *   DESC: Simple priority heap for discrete event simulation.
 */

#include "heap_array.h"
#include <string.h> // For memcpy(3)
#include <stdlib.h> // for rand(3)
#include <stdio.h>  // For printf(3)
#include <dbg.h>

const int STARTING_SIZE = 511;

#define HEAP_NODE(heap, index) (((node_t*)(heap->data))[index])

typedef struct node {
  void* data;
  long long key;
} node_t;

void down_heap(heap_t* heap, int index);
void up_heap(heap_t* heap, int index);
void swap(node_t* first, node_t* second);
node_t* prev(node_t* node);
node_t* next(node_t* next);

void init_node(node_t* node) {
  node->data = NULL;
  node->key = -1;
}

void init_heap(heap_t* heap) {
  heap->size = 0;
  heap->private_size = STARTING_SIZE;
  heap->data = malloc(sizeof(node_t) * heap->private_size);
}

int heap_size(heap_t* heap) {
  return heap->size;
}

int is_empty(heap_t* heap) {
  return heap->size == 0;
}

int heap_is_empty(heap_t* heap) {
  return is_empty(heap);
}

long long heap_get_min_key(heap_t* heap) {
  if (is_empty(heap)) {
    return -1;
  }
  else {
    return HEAP_NODE(heap, 0).key;
  }
}

void* heap_peek_min_data(heap_t* heap) {
  if (is_empty(heap)) {
    return NULL;
  }
  else {
    return HEAP_NODE(heap, 0).data;
  }
}

void* heap_pop_min_data(heap_t* heap, long long* key) {
  int last_index = heap->size - 1;
  void* data = HEAP_NODE(heap, 0).data;
  if (key != NULL) {
    *key = HEAP_NODE(heap, 0).key;
  }
  HEAP_NODE(heap, 0).data = HEAP_NODE(heap, last_index).data;
  HEAP_NODE(heap, 0).key = HEAP_NODE(heap, last_index).key;

  heap->size--;

  down_heap(heap, 0);

  return data;
}

void expand_heap(heap_t* heap) {
  int new_size = (heap->private_size * 2) + 1;
  void* new_data = malloc(sizeof(node_t) * new_size);

  dbg(DBG_SIM, ("Resized heap from %i to %i.\n", heap->private_size, new_size));
  
  memcpy(new_data, heap->data, (sizeof(node_t) * heap->private_size));
  free(heap->data);

  heap->data = new_data;
  heap->private_size = new_size;
  
}

void heap_insert(heap_t* heap, void* data, long long key) {
  int index = heap->size;
  if (index == heap->private_size) {
    expand_heap(heap);
  }
  
  index = heap->size;
  HEAP_NODE(heap, index).key = key;
  HEAP_NODE(heap, index).data = data;
  up_heap(heap, index);

  heap->size++;
}

void swap(node_t* first, node_t* second) {
  long long key;
  void* data;

  key = first->key;
  first->key = second->key;
  second->key = key;

  data = first->data;
  first->data = second->data;
  second->data = data;
}

void down_heap(heap_t* heap, int index) {
  int right_index =  ((index + 1) * 2);
  int left_index = (index * 2) + 1;

  if (right_index < heap->size) { // Two children
    long long left_key = HEAP_NODE(heap, left_index).key;
    long long right_key = HEAP_NODE(heap, right_index).key;
    int min_key_index = (left_key < right_key)? left_index : right_index;

    if (HEAP_NODE(heap, min_key_index).key < HEAP_NODE(heap, index).key) {
      swap(&(HEAP_NODE(heap, index)), &(HEAP_NODE(heap, min_key_index)));
      down_heap(heap, min_key_index);
    }
  }
  else if (left_index >= heap->size) { // No children
    return;
  }
  else { // Only left child
    long long left_key = HEAP_NODE(heap, left_index).key;
    if (left_key < HEAP_NODE(heap, index).key) {
      swap(&(HEAP_NODE(heap, index)), &(HEAP_NODE(heap, left_index)));
      return;
    }
  }
}

void up_heap(heap_t* heap, int index) {
  int parent_index;
  if (index == 0) {
    return;
  }

  parent_index = (index - 1) / 2;

  if (HEAP_NODE(heap, parent_index).key > HEAP_NODE(heap, index).key) {
    swap(&(HEAP_NODE(heap, index)), &(HEAP_NODE(heap, parent_index)));
    up_heap(heap, parent_index);
  }
}

