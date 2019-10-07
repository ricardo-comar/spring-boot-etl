package com.github.ricardocomar.springbootetl.etlconsumer.fixture;


import java.math.BigDecimal;
import java.math.RoundingMode;

import com.github.ricardocomar.springbootetl.etlconsumer.model.PurchaseItem;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

public class PurchaseItemModelFixture implements TemplateLoader {

	@Override
	public void load() {
		Fixture.of(PurchaseItem.class).addTemplate("g6play", new Rule() {
			{
				add("id", "123-45-6");
				add("sku", "sku123456");
				add("title", "Motorola G6 Play");
				add("value", new BigDecimal(500.00).setScale(2, RoundingMode.HALF_EVEN));
			}
		});
		Fixture.of(PurchaseItem.class).addTemplate("iphone10", new Rule() {
			{
				add("id", "654-32-1");
				add("sku", "sku654321");
				add("title", "Iphone 10");
				add("value", new BigDecimal(3900.00).setScale(2, RoundingMode.HALF_EVEN));
			}
		});
		Fixture.of(PurchaseItem.class).addTemplate("iphone11pro", new Rule() {
			{
				add("id", "999-99-9");
				add("sku", "sku999999");
				add("title", "Iphone 11 Pro");
				add("value", new BigDecimal(11500.00).setScale(2, RoundingMode.HALF_EVEN));
			}
		});
	}

}
