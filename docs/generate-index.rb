#!/usr/bin/env ruby

require 'asciidoctor'

if ARGV.empty?
  puts "usage: generate-index.rb <source file>"
  exit 1
end

Asciidoctor.convert_file ARGV.first, to_file: true, safe: :safe
