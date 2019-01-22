package contracts.beer.rest

import org.springframework.cloud.contract.spec.Contract

Contract contractDsl = Contract.make {
	request {
		method 'POST'
		urlPath '/cancelOrder'
		body """
<order>
<value>123</value>
<beerNames>
<beerName>abc</beerName>
<beerName>def</beerName>
<beerName>ghi</beerName>
</beerNames>
</order>"""
		headers {
			contentType(applicationXml())
		}
	}
	response {
		status(OK())
		headers {
			contentType(applicationXml())
		}
		body """
<sale>
<transactionUuid>uuid</transactionUuid>
<beerNames>
<beerName>abc</beerName>
<beerName>def</beerName>
<beerName>ghi</beerName>
</beerNames>
</sale>"""
	}
}