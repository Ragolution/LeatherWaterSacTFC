/**
 *  Copyright (C) 2013  emris
 *  https://github.com/emris/LeatherWaterSacTFC
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package emris.lwstfc;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import com.bioxx.tfc.TFCBlocks;
import com.bioxx.tfc.Core.TFCFluid;
import com.bioxx.tfc.Core.TFCTabs;
import com.bioxx.tfc.Core.TFC_Core;
import com.bioxx.tfc.Core.Player.FoodStatsTFC;
import com.bioxx.tfc.Items.ItemTerra;
import com.bioxx.tfc.api.Enums.EnumFoodGroup;
import com.bioxx.tfc.api.Enums.EnumItemReach;
import com.bioxx.tfc.api.Enums.EnumSize;
import com.bioxx.tfc.api.Enums.EnumWeight;
import com.bioxx.tfc.api.Interfaces.ISize;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemLeatherWaterSac extends Item implements ISize, IFluidContainerItem
{
	private int capacity;

	public ItemLeatherWaterSac()
	{
		super();
		this.maxStackSize = 1;
		this.capacity = 600;
		this.setCreativeTab(TFCTabs.TFCMisc);
		this.setMaxDamage(capacity);
		this.hasSubtypes = false;
		this.setUnlocalizedName("LeatherWaterSac");
		this.canStack();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void getSubItems(Item item, CreativeTabs tabs, List list)
	{
		list.add(new ItemStack(this, 1));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IIconRegister registerer)
	{
		this.itemIcon = registerer.registerIcon("lwstfc:LeatherWaterSac");
	}

	@Override
	public ItemStack onItemRightClick(ItemStack sac, World world, EntityPlayer player)
	{
		if(player.isSneaking())
		{
			this.drain(sac, capacity, true);
			return sac;
		}

		if (player.capabilities.isCreativeMode)
		{
			FluidStack fs = this.getFluid(sac);
			if(fs == null)
				sac.setItemDamage(sac.getMaxDamage());
			else
				this.fill(sac, new FluidStack(fs, capacity), true);
		}

		MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(world, player, true);
		if (mop == null)
		{
			if(sac.getItemDamage() == sac.getMaxDamage())
			{
				if(player instanceof EntityPlayerMP)
					player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("sac.empty")));
			}
			else
			{
				player.setItemInUse(sac, this.getMaxItemUseDuration(sac));
			}
			return sac;
		}
		else
		{
			if(mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
			{
				int x = mop.blockX;
				int y = mop.blockY;
				int z = mop.blockZ;

				if(!world.canMineBlock(player, x, y, z))
					return sac;

				if(!player.canPlayerEdit(x, y, z, mop.sideHit, sac))
					return sac;

				if(world.getBlock(x, y, z).getMaterial() == Material.water)
				{
					if(sac.getItemDamage() > 0)
					{
						fillSac(world, sac, x, y, z);
						world.spawnParticle("splash", x + (world.rand.nextDouble()-0.5), y + 1, z + (world.rand.nextDouble()-0.5), 0.0D, (world.rand.nextDouble()-0.5), 0.0D);
						world.spawnParticle("splash", x + (world.rand.nextDouble()-0.5), y + 2, z + (world.rand.nextDouble()-0.5), 0.0D, (world.rand.nextDouble()-0.5), 0.0D);
						world.spawnParticle("splash", x + (world.rand.nextDouble()-0.5), y + 1, z + (world.rand.nextDouble()-0.5), 0.0D, (world.rand.nextDouble()-0.5), 0.0D);
						world.spawnParticle("splash", x + (world.rand.nextDouble()-0.5), y + 2, z + (world.rand.nextDouble()-0.5), 0.0D, (world.rand.nextDouble()-0.5), 0.0D);
						world.spawnParticle("splash", x + (world.rand.nextDouble()-0.5), y + 1, z + (world.rand.nextDouble()-0.5), 0.0D, (world.rand.nextDouble()-0.5), 0.0D);
						world.spawnParticle("splash", x + (world.rand.nextDouble()-0.5), y + 2, z + (world.rand.nextDouble()-0.5), 0.0D, (world.rand.nextDouble()-0.5), 0.0D);
						world.spawnParticle("splash", x + (world.rand.nextDouble()-0.5), y + 1, z + (world.rand.nextDouble()-0.5), 0.0D, (world.rand.nextDouble()-0.5), 0.0D);
						world.spawnParticle("splash", x + (world.rand.nextDouble()-0.5), y + 2, z + (world.rand.nextDouble()-0.5), 0.0D, (world.rand.nextDouble()-0.5), 0.0D);
						world.playSoundAtEntity(player, "random.splash", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
					}
					else
					{
						player.setItemInUse(sac, this.getMaxItemUseDuration(sac));
					}
				}
				else
				{
					if(sac.getItemDamage() == sac.getMaxDamage())
					{
						if(player instanceof EntityPlayerMP)
							player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("sac.empty")));
					}
					else
					{
						player.setItemInUse(sac, this.getMaxItemUseDuration(sac));
					}
				}
			}
			return sac;
		}
	}

	private void fillSac(World world, ItemStack sac, int x, int y, int z)
	{
		Block b = world.getBlock(x, y, z);
		int amount = 100;

		if (b == TFCBlocks.FreshWater || b == TFCBlocks.FreshWaterStationary ||
			b == TFCBlocks.HotWater || b == TFCBlocks.HotWaterStationary)
		{
			FluidStack fs = FluidRegistry.getFluidStack(TFCFluid.FRESHWATER.getName(), amount);
			this.fill(sac, fs, true);
		}

		if (b == TFCBlocks.SaltWater || b == TFCBlocks.SaltWaterStationary)
		{
			FluidStack fs = FluidRegistry.getFluidStack(TFCFluid.SALTWATER.getName(), amount);
			this.fill(sac, fs, true);
		}
	}

	@Override
	public EnumAction getItemUseAction(ItemStack sac)
	{
		return EnumAction.drink;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack sac)
	{
		return 32;
	}

	@Override
	public ItemStack onEaten(ItemStack sac, World world, EntityPlayer player)
	{
		if(!world.isRemote)
		{
			if(sac.getItemDamage() != sac.getMaxDamage() || player.capabilities.isCreativeMode)
			{
				if(player instanceof EntityPlayerMP)
				{
					EntityPlayerMP p = (EntityPlayerMP)player;
					FoodStatsTFC fs = TFC_Core.getPlayerFoodStats(p);
					float nwl = fs.getMaxWater(p);
					int rw = (int)nwl / 6;

					FluidStack sacFS = this.getFluid(sac);
					if(sacFS == null)
						return sac;

					if (sacFS.getFluid() == TFCFluid.FRESHWATER)
					{
						if (fs.needDrink())
						{
							fs.restoreWater(p, rw);
							if (!p.capabilities.isCreativeMode)
								this.drain(sac, 50, true);
						}
						else
						{
							world.playSoundAtEntity(p, "random.burp", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
							p.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("drink.full")));
						}
					}
					else if (sacFS.getFluid() == TFCFluid.SALTWATER && fs.needDrink())
					{
						fs.restoreWater(p, -rw);
						if (!p.capabilities.isCreativeMode)
							this.drain(sac, 50, true);
						p.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("drink.salt")));
						//Do kind of spit animation
						p.getServerForPlayer().getEntityTracker().func_151248_b(p, new S0BPacketAnimation(p, 4));
					}
					else if (sacFS.getFluid() == TFCFluid.BEER
							|| sacFS.getFluid() == TFCFluid.CIDER
							|| sacFS.getFluid() == TFCFluid.RUM
							|| sacFS.getFluid() == TFCFluid.RYEWHISKEY
							|| sacFS.getFluid() == TFCFluid.SAKE
							|| sacFS.getFluid() == TFCFluid.VODKA
							|| sacFS.getFluid() == TFCFluid.WHISKEY
							|| sacFS.getFluid() == TFCFluid.CORNWHISKEY)
					{
						if (fs.needDrink())
						{
							fs.restoreWater(p, 800);
							if (!p.capabilities.isCreativeMode)
								this.drain(sac, 50, true);
	
							long soberTime = p.getEntityData().hasKey("soberTime") ? p.getEntityData().getLong("soberTime") : 0;
							int time = world.rand.nextInt(1000) + 400;
							soberTime += time;
	
							if(world.rand.nextInt(100) == 0) p.addPotionEffect(new PotionEffect(8, time, 4));
							if(world.rand.nextInt(100) == 0) p.addPotionEffect(new PotionEffect(5, time, 4));
							if(world.rand.nextInt(100) == 0) p.addPotionEffect(new PotionEffect(8, time, 4));
							if(world.rand.nextInt(200) == 0) p.addPotionEffect(new PotionEffect(10, time, 4));
							if(world.rand.nextInt(150) == 0) p.addPotionEffect(new PotionEffect(12, time, 4));
							if(world.rand.nextInt(180) == 0) p.addPotionEffect(new PotionEffect(13, time, 4));
	
							int levelMod = 250 * p.experienceLevel;
							if(soberTime > 3000 + levelMod)
							{
								if(world.rand.nextInt(4) == 0)
								{
									//player.addPotionEffect(new PotionEffect(9,time,4));
								}
	
								if(soberTime > 5000 + levelMod)
								{
									if(world.rand.nextInt(4) == 0)
										p.addPotionEffect(new PotionEffect(18, time, 4));
	
									if(soberTime > 7000 + levelMod)
									{
										if(world.rand.nextInt(2) == 0)
											p.addPotionEffect(new PotionEffect(15, time, 4));
	
										if(soberTime > 8000 + levelMod)
										{
											if(world.rand.nextInt(10) == 0)
												p.addPotionEffect(new PotionEffect(20, time, 4));
										}
	
										if(soberTime > 10000 + levelMod && !p.capabilities.isCreativeMode)
										{
											soberTime = 0;
											//((EntityPlayerMP)player).mcServer.getConfigurationManager().sendChatMsg(player.username+" died of alcohol poisoning.");
											p.inventory.dropAllItems();
											p.setHealth(0);
										}
									}
								}
							}
							p.getEntityData().setLong("soberTime",soberTime);
						}
						else
						{
							world.playSoundAtEntity(p, "random.burp", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
							p.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("drink.full")));
						}
					}
					else if(sacFS.getFluid() == TFCFluid.MILK && fs.needFood())
					{
						fs.restoreWater(p, rw);
						if (!p.capabilities.isCreativeMode)
							this.drain(sac, 50, true);
						fs.addNutrition(EnumFoodGroup.Dairy, 20);
					}
				}
			}
		}
		return sac;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void addInformation(ItemStack is, EntityPlayer player, List arraylist, boolean flag)
	{
		ItemTerra.addSizeInformation(is, arraylist);
		FluidStack fs = this.getFluid(is);
		if(fs != null)
			arraylist.add(EnumChatFormatting.DARK_GRAY + fs.getLocalizedName() + ":" + fs.amount + ":" + is.getItemDamage());
	}

	@Override
	public EnumSize getSize(ItemStack is)
	{
		return EnumSize.SMALL;
	}

	@Override
	public EnumWeight getWeight(ItemStack is)
	{
		return EnumWeight.LIGHT;
	}

	@Override
	public boolean canStack()
	{
		return false;
	}

	@Override
	public EnumItemReach getReach(ItemStack is)
	{
		return EnumItemReach.SHORT;
	}


	////////////////////////////////////////////////////
	/*
	 * Fluid stuff
	 */
	@Override
	public FluidStack getFluid(ItemStack container)
	{
		if (container.stackTagCompound == null || !container.stackTagCompound.hasKey("Fluid"))
			return null;

		return FluidStack.loadFluidStackFromNBT(container.stackTagCompound.getCompoundTag("Fluid"));
	}

	@Override
	public int getCapacity(ItemStack container)
	{
		return this.capacity;
	}

	@Override
	public int fill(ItemStack container, FluidStack resource, boolean doFill)
	{
		if (resource == null || !isValidFluid(resource))
			return 0;

		if (!doFill)
		{
			if (container.stackTagCompound == null || !container.stackTagCompound.hasKey("Fluid"))
				return Math.min(capacity, resource.amount);

			FluidStack stack = FluidStack.loadFluidStackFromNBT(container.stackTagCompound.getCompoundTag("Fluid"));

			if (stack == null)
				return Math.min(capacity, resource.amount);

			if (!stack.isFluidEqual(resource))
				return 0;

			return Math.min(capacity - stack.amount, resource.amount);
		}

		if (container.stackTagCompound == null)
			container.stackTagCompound = new NBTTagCompound();

		if (!container.stackTagCompound.hasKey("Fluid"))
		{
			NBTTagCompound fluidTag = resource.writeToNBT(new NBTTagCompound());

			if (capacity < resource.amount)
			{
				fluidTag.setInteger("Amount", capacity);
				container.stackTagCompound.setTag("Fluid", fluidTag);
				container.setItemDamage(0);
				return capacity;
			}

			container.stackTagCompound.setTag("Fluid", fluidTag);
			container.setItemDamage(capacity - resource.amount);
			return resource.amount;
		}

		NBTTagCompound fluidTag = container.stackTagCompound.getCompoundTag("Fluid");
		FluidStack stack = FluidStack.loadFluidStackFromNBT(fluidTag);

		if (!stack.isFluidEqual(resource))
			return 0;

		int filled = capacity - stack.amount;
		if (resource.amount < filled)
		{
			stack.amount += resource.amount;
			filled = resource.amount;
		}
		else
		{
			stack.amount = capacity;
		}

		container.stackTagCompound.setTag("Fluid", stack.writeToNBT(fluidTag));
		container.setItemDamage(capacity - stack.amount);
		return filled;
	}

	@Override
	public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain)
	{
		if (container.stackTagCompound == null || !container.stackTagCompound.hasKey("Fluid"))
			return null;

		FluidStack stack = FluidStack.loadFluidStackFromNBT(container.stackTagCompound.getCompoundTag("Fluid"));
		if (stack == null)
			return null;

		int currentAmount = stack.amount;
		stack.amount = Math.min(stack.amount, maxDrain);
		if (doDrain)
		{
			if (currentAmount == stack.amount)
			{
				container.stackTagCompound.removeTag("Fluid");
				if (container.stackTagCompound.hasNoTags())
					container.stackTagCompound = null;
				container.setItemDamage(container.getMaxDamage());
				return stack;
			}

			NBTTagCompound fluidTag = container.stackTagCompound.getCompoundTag("Fluid");
			fluidTag.setInteger("Amount", currentAmount - stack.amount);
			container.stackTagCompound.setTag("Fluid", fluidTag);
			container.setItemDamage(capacity - (currentAmount - stack.amount));
		}
		return stack;
	}

	private boolean isValidFluid(FluidStack fs)
	{
		return fs.getFluid() == TFCFluid.BEER
				|| fs.getFluid() == TFCFluid.CIDER
				|| fs.getFluid() == TFCFluid.CORNWHISKEY
				|| fs.getFluid() == TFCFluid.FRESHWATER
				|| fs.getFluid() == TFCFluid.HOTWATER
				|| fs.getFluid() == TFCFluid.MILK
				|| fs.getFluid() == TFCFluid.RUM
				|| fs.getFluid() == TFCFluid.RYEWHISKEY
				|| fs.getFluid() == TFCFluid.SAKE
				|| fs.getFluid() == TFCFluid.SALTWATER
				|| fs.getFluid() == TFCFluid.VODKA
				|| fs.getFluid() == TFCFluid.WHISKEY;
	}
}