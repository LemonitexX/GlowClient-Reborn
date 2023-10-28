/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */

package net.ccbluex.liquidbounce.injection.backend

import net.ccbluex.liquidbounce.api.minecraft.client.render.texture.IAbstractTexture
import net.ccbluex.liquidbounce.api.minecraft.client.render.texture.ITextureManager
import net.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation
import net.minecraft.client.renderer.texture.TextureManager
import net.minecraft.util.ResourceLocation

class TextureManagerImpl(val wrapped: TextureManager) : ITextureManager {
    override fun loadTexture(textureLocation: IResourceLocation, textureObj: IAbstractTexture): Boolean = wrapped.loadTexture(textureLocation.unwrap(), textureObj.unwrap())

    override fun bindTexture(image: IResourceLocation) = wrapped.bindTexture(image.unwrap())

    override fun equals(other: Any?): Boolean {
        return other is TextureManagerImpl && other.wrapped == this.wrapped

    }
    override fun bindTexture2(image: ResourceLocation) = wrapped.bindTexture(image.also { unwrap() })
}

inline fun ITextureManager.unwrap(): TextureManager = (this as TextureManagerImpl).wrapped
inline fun TextureManager.wrap(): ITextureManager = TextureManagerImpl(this)