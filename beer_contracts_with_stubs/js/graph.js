/*  Graph JavaScript framework, version 0.0.1
 *  (c) 2006 Aslak Hellesoy <aslak.hellesoy@gmail.com>
 *  (c) 2006 Dave Hoover <dave.hoover@gmail.com>
 *
 *  Ported from Graph::Layouter::Spring in
 *    https://search.cpan.org/~pasky/Graph-Layderer-0.02/
 *  The algorithm is based on a spring-style layouter of a Java-based social
 *  network tracker PieSpy written by Paul Mutton E<lt>paul@jibble.orgE<gt>.
 *
 *  Adopted by Philipp Strathausen <strathausen@gmail.com> to support Raphael JS
 *  for rendering, dragging and much more. See https://blog.ameisenbar.de/
 *
 *  Graph is freely distributable under the terms of an MIT-style license.
 *  For details, see the Graph web site: https://dev.buildpatternd.com/trac
 *
 *  Links:
 *
 *  Demo of the original applet:
 *      https://redsquirrel.com/dave/work/webdep/
 *
 *  Mirrored original source code at snipplr:
 *      https://snipplr.com/view/1950/graph-javascript-framework-version-001/
 *
 *  Original usage example:
 *      http://ajaxian.com/archives/new-javascriptcanvas-graph-library
 *
/*--------------------------------------------------------------------------*/

/*
 * Graph
 */
var Graph = function() {
    this.nodes = [];
    this.edges = [];
};
Graph.prototype = {
    addNode: function(id, content) {
        /* testing if node is already existing in the graph */
        var new_node = this.nodes[id];
        if(new_node == undefined) {
                new_node = new Graph.Node(id, content||{"id":id});
                this.nodes[id] = new_node;
                this.nodes.push(new_node); // TODO get rid of the array
        }
        return new_node;
    },

    addEdge: function(source, target, style) {
        var s = this.addNode(source);
        var t = this.addNode(target);
        var color;
        var colorbg;
        var directed;
        if(style) { color = style.color; colorbg = style.colorbg; directed = style.directed }
        var edge = { source: s, target: t, color: color, colorbg: colorbg, directed: directed };
        this.edges.push(edge);
    }
};

/*
 * Node
 */
Graph.Node = function(id, value){
    this.id = id;
    this.content = value;
};
Graph.Node.prototype = {
};
Graph.Renderer = {};
Graph.Renderer.Raphael = function(element, graph, width, height) {
    this.width = width||400;
    this.height = height||400;
    var selfRef = this;
    this.r = Raphael(element, this.width, this.height);
    this.radius = 40; /* max dimension of a node */
    this.graph = graph;
    this.mouse_in = false;
    
    /*
     * Dragging
     */
    this.isDrag = false;
    this.dragger = function (e) {
        this.dx = e.clientX;
        this.dy = e.clientY;
        selfRef.isDrag = this;
        this.animate({"fill-opacity": .2}, 500);
        e.preventDefault && e.preventDefault();
    };

    document.onmousemove = function (e) {
        e = e || window.event;
        if (selfRef.isDrag) {
            var newX = e.clientX - selfRef.isDrag.dx + (selfRef.isDrag.attrs.cx == null ? (selfRef.isDrag.attrs.x + selfRef.isDrag.attrs.width / 2) : selfRef.isDrag.attrs.cx);
            var newY = e.clientY - selfRef.isDrag.dy + (selfRef.isDrag.attrs.cy == null ? (selfRef.isDrag.attrs.y + selfRef.isDrag.attrs.height / 2) : selfRef.isDrag.attrs.cy);
            /* prevent shapes from being dragged out of the canvas */
            var clientX = e.clientX - (newX < 20 ? newX - 20 : newX > selfRef.width - 20 ? newX - selfRef.width + 20 : 0);
            var clientY = e.clientY - (newY < 20 ? newY - 20 : newY > selfRef.height - 20 ? newY - selfRef.height + 20 : 0);
            selfRef.isDrag.translate(clientX - selfRef.isDrag.dx, clientY - selfRef.isDrag.dy);
            selfRef.isDrag.label.translate(clientX - selfRef.isDrag.dx, clientY - selfRef.isDrag.dy);
            for (var i in selfRef.graph.edges) {
                selfRef.graph.edges[i].connection.draw();
            }
            //selfRef.r.safari();
            selfRef.isDrag.dx = clientX;
            selfRef.isDrag.dy = clientY;
        }
    };
    document.onmouseup = function () {
        selfRef.isDrag && selfRef.isDrag.animate({"fill-opacity": 0}, 500);
        selfRef.isDrag = false;
    };
};

/*
 * Renderer using RaphaelJS
 */
Graph.Renderer.Raphael.prototype = {
    translate: function(point) {
        return [
            (point[0] - this.graph.layoutMinX) * this.factorX + this.radius,
            (point[1] - this.graph.layoutMinY) * this.factorY + this.radius
        ];
    },

    rotate: function(point, length, angle) {
        var dx = length * Math.cos(angle);
        var dy = length * Math.sin(angle);
        return [point[0]+dx, point[1]+dy];
    },

    draw: function() {
        this.factorX = (width - 2 * this.radius) / (this.graph.layoutMaxX - this.graph.layoutMinX);
        this.factorY = (height - 2 * this.radius) / (this.graph.layoutMaxY - this.graph.layoutMinY);
        for (var i = 0; i < this.graph.nodes.length; i++) {
            this.drawNode(this.graph.nodes[i]);
        }
        for (var i = 0; i < this.graph.edges.length; i++) {
            this.drawEdge(this.graph.edges[i]);
        }
    },
    drawNode: function(node) {
        var point = this.translate([node.layoutPosX, node.layoutPosY]);
        node.point = point;
        
        /* if node has already been drawn, move the nodes */
        if(node.shape) {
//            console.log(node.shape.attrs );
            var opoint = [ node.shape.attrs.cx || node.shape.attrs.x + node.shape.attrs.width / 2 , node.shape.attrs.cy || node.shape.attrs.y + node.shape.attrs.height / 2 + 15 ];
            node.shape.translate(point[0]-opoint[0], point[1]-opoint[1]);
            node.shape.label.translate(point[0]-opoint[0], point[1]-opoint[1]);
            this.r.safari();
            return;
        }
        var shape;
        if(node.content.getShape) {
            shape = node.content.getShape(this.r, point[0], point[1]);
            shape.attr({"fill-opacity": 0});
        } else {
            shape = this.r.ellipse(point[0], point[1], 30, 20);
            var color = Raphael.getColor();
            shape.attr({fill: color, stroke: color, "fill-opacity": 0, "stroke-width": 2})
        }
        shape.mousedown(this.dragger);
        shape.node.style.cursor = "move";
        shape.label = this.r.text(point[0], point[1] + 30, node.content.label || node.id); // Beware: operator || also considers values like -1, 0, ...
        node.shape = shape;
    },
    drawEdge: function(edge) {
        /* if edge already has been drawn, only refresh the edge */
        edge.connection && edge.connection.draw();
        if(!edge.connection)
            edge.connection = this.r.connection(edge.source.shape, edge.target.shape, { fg: edge.color, bg: edge.colorbg, directed: edge.directed });
    }
};
Graph.Layout = {};
Graph.Layout.Spring = function(graph) {
                this.graph = graph;
                this.iterations = 500;
                this.maxRepulsiveForceDistance = 6;
                this.k = 2;
                this.c = 0.01;
                this.maxVertexMovement = 0.5;
        };
Graph.Layout.Spring.prototype = {
        layout: function() {
                this.layoutPrepare();
            for (var i = 0; i < this.iterations; i++) {
                        this.layoutIteration();
                }
                this.layoutCalcBounds();
        },
       
        layoutPrepare: function() {
            for (var i = 0; i < this.graph.nodes.length; i++) {
                    var node = this.graph.nodes[i];
                        node.layoutPosX = 0;
                        node.layoutPosY = 0;
                        node.layoutForceX = 0;
                        node.layoutForceY = 0;
                }
                
        },
       
        layoutCalcBounds: function() {
                var minx = Infinity, maxx = -Infinity, miny = Infinity, maxy = -Infinity;

            for (var i = 0; i < this.graph.nodes.length; i++) {
                        var x = this.graph.nodes[i].layoutPosX;
                        var y = this.graph.nodes[i].layoutPosY;
                                               
                        if(x > maxx) maxx = x;
                        if(x < minx) minx = x;
                        if(y > maxy) maxy = y;
                        if(y < miny) miny = y;
                }

                this.graph.layoutMinX = minx;
                this.graph.layoutMaxX = maxx;
                this.graph.layoutMinY = miny;
                this.graph.layoutMaxY = maxy;
        },
       
        layoutIteration: function() {
                // Forces on nodes due to node-node repulsions
            for (var i = 0; i < this.graph.nodes.length; i++) {
                    var node1 = this.graph.nodes[i];
                    for (var j = i + 1; j < this.graph.nodes.length; j++) {
                            var node2 = this.graph.nodes[j];
                                this.layoutRepulsive(node1, node2);
                        }
                }
                // Forces on nodes due to edge attractions
            for (var i = 0; i < this.graph.edges.length; i++) {
                    var edge = this.graph.edges[i];
                        this.layoutAttractive(edge);             
                }
               
                // Move by the given force
            for (var i = 0; i < this.graph.nodes.length; i++) {
                    var node = this.graph.nodes[i];
                        var xmove = this.c * node.layoutForceX;
                        var ymove = this.c * node.layoutForceY;

                        var max = this.maxVertexMovement;
                        if(xmove > max) xmove = max;
                        if(xmove < -max) xmove = -max;
                        if(ymove > max) ymove = max;
                        if(ymove < -max) ymove = -max;
                       
                        node.layoutPosX += xmove;
                        node.layoutPosY += ymove;
                        node.layoutForceX = 0;
                        node.layoutForceY = 0;
                }
        },

        layoutRepulsive: function(node1, node2) {
                var dx = node2.layoutPosX - node1.layoutPosX;
                var dy = node2.layoutPosY - node1.layoutPosY;
                var d2 = dx * dx + dy * dy;
                if(d2 < 0.01) {
                        dx = 0.1 * Math.random() + 0.1;
                        dy = 0.1 * Math.random() + 0.1;
                        var d2 = dx * dx + dy * dy;
                }
                var d = Math.sqrt(d2);
                if(d < this.maxRepulsiveForceDistance) {
                        var repulsiveForce = this.k * this.k / d;
                        node2.layoutForceX += repulsiveForce * dx / d;
                        node2.layoutForceY += repulsiveForce * dy / d;
                        node1.layoutForceX -= repulsiveForce * dx / d;
                        node1.layoutForceY -= repulsiveForce * dy / d;
                }
        },

        layoutAttractive: function(edge) {
                var node1 = edge.source;
                var node2 = edge.target;
               
                var dx = node2.layoutPosX - node1.layoutPosX;
                var dy = node2.layoutPosY - node1.layoutPosY;
                var d2 = dx * dx + dy * dy;
                if(d2 < 0.01) {
                        dx = 0.1 * Math.random() + 0.1;
                        dy = 0.1 * Math.random() + 0.1;
                        var d2 = dx * dx + dy * dy;
                }
                var d = Math.sqrt(d2);
                if(d > this.maxRepulsiveForceDistance) {
                        d = this.maxRepulsiveForceDistance;
                        d2 = d * d;
                }
                var attractiveForce = (d2 - this.k * this.k) / this.k;
                if(edge.weight == undefined || edge.weight < 1) edge.weight = 1;
                attractiveForce *= Math.log(edge.weight) * 0.5 + 1;
               
                node2.layoutForceX -= attractiveForce * dx / d;
                node2.layoutForceY -= attractiveForce * dy / d;
                node1.layoutForceX += attractiveForce * dx / d;
                node1.layoutForceY += attractiveForce * dy / d;
        }
};
