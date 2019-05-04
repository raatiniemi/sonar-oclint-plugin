/*
 * Copyright (c) 2019 Tobias Raatiniemi
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.raatiniemi.sonar.oclint.report

import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList

internal fun getElements(document: Document, tagName: String): Collection<Element> {
    val nodeList = document.getElementsByTagName(tagName)

    return parseElementsFromNodeList(nodeList)
}

private fun parseElementsFromNodeList(nodeList: NodeList) =
    (0 until nodeList.length)
        .mapNotNull(findElement(nodeList))
        .toList()

private fun findElement(nodeList: NodeList): (Int) -> Element? = {
    val node = nodeList.item(it)
    if (isElement(node)) {
        node as Element
    } else {
        null
    }
}

private fun isElement(node: Node) = node.nodeType == Node.ELEMENT_NODE
