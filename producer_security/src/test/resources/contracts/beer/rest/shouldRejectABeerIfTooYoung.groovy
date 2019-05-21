package contracts.beer.rest

import org.springframework.cloud.contract.spec.Contract

Contract.make {
	description("""
Represents an unsuccessful scenario of getting a beer

```
given:
	client is not old enough
when:
	he applies for a beer
then:
	we'll NOT grant him the beer
```

""")
	request {
		method 'POST'
		urlPath '/check'
		headers {
			contentType(applicationJsonUtf8())
			header(authorization(), "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJERmJmZWVpR29ySXMyZ1VUaDA2d2hfZVZKTUc2azU5X0dVZzBrOVRLb05vIn0.eyJqdGkiOiJiMmY2M2U4Yi04N2ExLTRiYzgtYTEwMC0zMWMwNzdmNTFiYTIiLCJleHAiOjIwMzA3ODA2MDIsIm5iZiI6MCwiaWF0IjoxNTU3ODI3MDAyLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0Ojg3NjUvYXV0aC9yZWFsbXMvc3ByaW5nX2Nsb3VkX2NvbnRyYWN0cyIsImF1ZCI6InNwcmluZ19jbG91ZF9jb250cmFjdHMiLCJzdWIiOiI4NGViYjM2MS0wZWZjLTRmOTgtYTlhMS0wZDY3ZDE2N2I2MGQiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJzcHJpbmdfY2xvdWRfY29udHJhY3RzIiwiYXV0aF90aW1lIjowLCJzZXNzaW9uX3N0YXRlIjoiNzJkOWUxN2MtOTIyNC00MGFmLWFkNDQtMWRhNGZmNDhiNzZiIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyIqIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJjdXN0b21lciJdfSwicmVzb3VyY2VfYWNjZXNzIjp7fSwic2NvcGUiOiIiLCJhdWQiOiJvYXV0aDItcmVzb3VyY2UiLCJ1c2VyX25hbWUiOiJ0b28geW91bmciLCJ1c2VyX2RldGFpbHMiOnsiYWdlIjoxNiwidXNlcm5hbWUiOiJ0b28geW91bmcifSwiYXV0aG9yaXRpZXMiOlsiY3VzdG9tZXIiXX0.Ta182t7wEqzjisSPGcGNANyPdJvzL2BeDFuKHc5T7nwAJJvqn6pT9mtNpuMajaFcRfwr1dhCs5KqOlINuRsgfUFm3o7-kyg-DoS-BurwFvBmkiXeQuYHTBEYpBAI6wTtj8b8nMa0Id16-yv1Wkr82lBS4FiQp_sf5etOsYGUNU__0UUuYDiacZlTJ3kn-m7HA1OvYQm8ccAXX9NZ5AqkrVanGf6LolVJFFQ4uigVR3POjR5S-yFyikAj8PRdPp4UrlB_60S6DRxDxZHIBz4um0c_IZGfHuw9MLq3tr_AozoL57mdSzo58l2_oYWW2eAQfN6HyluRUKAKgmWOcT1v2A")
		}
	}
	response {
		status 200
		body("""
	{
		"status": "NOT_OK"
	}
	""")
		headers {
			contentType(applicationJson())
		}
		bodyMatchers {
			jsonPath('$.status', byType())
		}
	}
}