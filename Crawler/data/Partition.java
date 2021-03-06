9
https://raw.githubusercontent.com/ishugaliy/allgood-consistent-hash/master/src/main/java/org/ishugaliy/allgood/consistent/hash/partition/Partition.java
/*
 * The MIT License
 *
 * Copyright (c) 2020 Iurii Shugalii
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.ishugaliy.allgood.consistent.hash.partition;

import org.ishugaliy.allgood.consistent.hash.node.Node;

/**
 * Base interface of partitions to be used inside {@link org.ishugaliy.allgood.consistent.hash.HashRing}.
 * Partitions are created for each {@link Node} that added to the ring,
 * with assigned slots (ring positions) based on {@link Partition#getPartitionKey()}.
 *
 * @param <T> the type of node to be partitioned
 *
 * @author Iurii Shugalii
 */
public interface Partition<T extends Node> {

    T getNode();

    long getSlot();

    void setSlot(long slot);

    /**
     * Return the partition key, used for slot calculation.
     *
     * @return the partition key
     */
    String getPartitionKey();
}